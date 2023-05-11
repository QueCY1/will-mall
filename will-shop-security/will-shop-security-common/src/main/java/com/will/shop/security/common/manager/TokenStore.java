package com.will.shop.security.common.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import com.will.shop.common.constants.OauthCacheNames;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ResponseEnum;
import com.will.shop.common.util.PrincipalUtil;
import com.will.shop.security.common.bo.TokenInfoBO;
import com.will.shop.security.common.bo.UserInfoInTokenBO;
import com.will.shop.security.common.enums.SysTypeEnum;
import com.will.shop.security.common.vo.TokenInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * token管理 1. 登陆返回token 2. 刷新token 3. 清除用户过去token 4. 校验token
 *
 * @author will
 * @date 2023/05/06
 */
@Component
public class TokenStore {

    private static final Logger logger = LoggerFactory.getLogger(TokenStore.class);

    /**
     * 用于aes签名的key，16位
     */
    @Value("${auth.token.signKey:will-mall-token-}")
    public String tokenSignKey;

    private final RedisTemplate<String, Object> redisTemplate;

    private final RedisSerializer<Object> redisSerializer;

    private final StringRedisTemplate stringRedisTemplate;

    public TokenStore(RedisTemplate<String, Object> redisTemplate,
                      StringRedisTemplate stringRedisTemplate, GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        this.redisTemplate = redisTemplate;
        this.redisSerializer = genericJackson2JsonRedisSerializer;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    /**
     * 将用户的部分信息存储在token中，并返回token信息
     *
     * @param userInfoInToken 用户在token中的信息
     * @return token信息
     */
    public TokenInfoBO storeAccessToken(UserInfoInTokenBO userInfoInToken) {
        TokenInfoBO tokenInfoBO = new TokenInfoBO();
        String accessToken = IdUtil.simpleUUID();
        String refreshToken = IdUtil.simpleUUID();

        tokenInfoBO.setUserInfoInToken(userInfoInToken);
        tokenInfoBO.setExpiresIn(getExpiresIn(userInfoInToken.getSysType()));

        String uidToAccessKeyStr = getUserIdToAccessKey(getApprovalKey(userInfoInToken));
        String accessKeyStr = getAccessKey(accessToken);
        String refreshToAccessKeyStr = getRefreshToAccessKey(refreshToken);

        // 一个用户会登陆很多次，每次登陆的token都会存在 uid_to_access里面
        // 但是每次保存都会更新这个key的时间，而key里面的token有可能会过期，过期就要移除掉
        List<byte[]> existsAccessTokensBytes = new ArrayList<>();
        // 新的token数据
        existsAccessTokensBytes.add((accessToken + StrUtil.COLON + refreshToken).getBytes(StandardCharsets.UTF_8));

        //在redis的集合中查找指定键（uidToAccessKeyStr）的集合个数
        Long size = redisTemplate.opsForSet().size(uidToAccessKeyStr);
        if (size != null && size != 0) {
            List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidToAccessKeyStr, size);
            if (tokenInfoBoList != null) {
                for (String accessTokenWithRefreshToken : tokenInfoBoList) {
                    String[] accessTokenWithRefreshTokenArr = accessTokenWithRefreshToken.split(StrUtil.COLON);
                    String accessTokenData = accessTokenWithRefreshTokenArr[0];
                    //如果token已经过期，那么不再加入到existsAccessTokensBytes中，这样再次存到redis中就可以清除掉过期的值了
                    if (BooleanUtil.isTrue(stringRedisTemplate.hasKey(getAccessKey(accessTokenData)))) {
                        existsAccessTokensBytes.add(accessTokenWithRefreshToken.getBytes(StandardCharsets.UTF_8));
                    }
                }
            }
        }

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {

            long expiresIn = tokenInfoBO.getExpiresIn();

            byte[] uidKey = uidToAccessKeyStr.getBytes(StandardCharsets.UTF_8);
            byte[] refreshKey = refreshToAccessKeyStr.getBytes(StandardCharsets.UTF_8);
            byte[] accessKey = accessKeyStr.getBytes(StandardCharsets.UTF_8);

            connection.sAdd(uidKey, ArrayUtil.toArray(existsAccessTokensBytes, byte[].class));

            // 通过uid + sysType 保存access_token，当需要禁用用户的时候，可以根据uid + sysType 禁用用户
            connection.expire(uidKey, expiresIn);

            // 通过refresh_token获取用户的access_token从而刷新token
            connection.setEx(refreshKey, expiresIn, accessToken.getBytes(StandardCharsets.UTF_8));

            // 通过access_token保存用户的商户id，用户id，uid
            connection.setEx(accessKey, expiresIn, Objects.requireNonNull(redisSerializer.serialize(userInfoInToken)));

            return null;
        });

        // 返回给前端是加密的token
        tokenInfoBO.setAccessToken(encryptToken(accessToken, userInfoInToken.getSysType()));
        tokenInfoBO.setRefreshToken(encryptToken(refreshToken, userInfoInToken.getSysType()));

        return tokenInfoBO;
    }

    private int getExpiresIn(int sysType) {
        // 3600秒
        int expiresIn = 3600;

        // 普通用户token过期时间 1天
        if (Objects.equals(sysType, SysTypeEnum.ORDINARY.value())) {
            expiresIn = expiresIn * 24;
        }
        // 系统管理员的token过期时间 1个月
        if (Objects.equals(sysType, SysTypeEnum.ADMIN.value())) {
            expiresIn = expiresIn * 24 * 30;
        }
        return expiresIn;
    }

    /**
     * 根据accessToken 获取用户信息
     *
     * @param accessToken accessToken
     * @param needDecrypt 是否需要解密
     * @return 用户信息
     */
    public UserInfoInTokenBO getUserInfoByAccessToken(String accessToken, boolean needDecrypt) {
        if (StrUtil.isBlank(accessToken)) {
            throw new WillShopBindException(ResponseEnum.UNAUTHORIZED, "accessToken 为空");
        }
        String realAccessToken;
        if (needDecrypt) {
            realAccessToken = decryptToken(accessToken);
        } else {
            realAccessToken = accessToken;
        }
        UserInfoInTokenBO userInfoInTokenBO = (UserInfoInTokenBO) redisTemplate.opsForValue()
                .get(getAccessKey(realAccessToken));

        if (userInfoInTokenBO == null) {
            throw new WillShopBindException(ResponseEnum.UNAUTHORIZED, "accessToken 已过期");
        }
        return userInfoInTokenBO;
    }

    /**
     * 刷新token，并返回新的token
     *
     * @param refreshToken 刷新token
     * @return 返回新的token
     */
    public TokenInfoBO refreshToken(String refreshToken) {
        if (StrUtil.isBlank(refreshToken)) {
            throw new WillShopBindException(ResponseEnum.UNAUTHORIZED, "refreshToken is blank");
        }
        String realRefreshToken = decryptToken(refreshToken);
        String accessToken = stringRedisTemplate.opsForValue().get(getRefreshToAccessKey(realRefreshToken));

        if (StrUtil.isBlank(accessToken)) {
            throw new WillShopBindException(ResponseEnum.UNAUTHORIZED, "refreshToken 已过期");
        }
        UserInfoInTokenBO userInfoInTokenBO = getUserInfoByAccessToken(accessToken,
                false);

        // 删除旧的refresh_token
        stringRedisTemplate.delete(getRefreshToAccessKey(realRefreshToken));
        // 删除旧的access_token
        stringRedisTemplate.delete(getAccessKey(accessToken));
        // 保存一份新的token
        return storeAccessToken(userInfoInTokenBO);
    }

    /**
     * 删除全部的token
     */
    public void deleteAllToken(String sysType, String userId) {
        String uidKey = getUserIdToAccessKey(getApprovalKey(sysType, userId));
        Long size = redisTemplate.opsForSet().size(uidKey);
        if (size == null || size == 0) {
            return;
        }
        List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidKey, size);

        if (CollUtil.isEmpty(tokenInfoBoList)) {
            return;
        }

        for (String accessTokenWithRefreshToken : tokenInfoBoList) {
            String[] accessTokenWithRefreshTokenArr = accessTokenWithRefreshToken.split(StrUtil.COLON);
            String accessToken = accessTokenWithRefreshTokenArr[0];
            String refreshToken = accessTokenWithRefreshTokenArr[1];
            redisTemplate.delete(getRefreshToAccessKey(refreshToken));
            redisTemplate.delete(getAccessKey(accessToken));
        }
        redisTemplate.delete(uidKey);

    }

    private static String getApprovalKey(UserInfoInTokenBO userInfoInToken) {
        return getApprovalKey(userInfoInToken.getSysType().toString(), userInfoInToken.getUserId());
    }

    private static String getApprovalKey(String sysType, String userId) {
        return userId == null ? sysType : sysType + StrUtil.COLON + userId;
    }

    private String encryptToken(String accessToken, Integer sysType) {
        AES aes = new AES(tokenSignKey.getBytes(StandardCharsets.UTF_8));
        return aes.encryptBase64(accessToken + System.currentTimeMillis() + sysType);
    }

    private String decryptToken(String data) {
        AES aes = new AES(tokenSignKey.getBytes(StandardCharsets.UTF_8));
        String decryptStr;
        String decryptToken;
        try {
            decryptStr = aes.decryptStr(data);
            decryptToken = decryptStr.substring(0, 32);
            // 创建token的时间，token使用时效性，防止攻击者通过一堆的尝试找到aes的密码，虽然aes是目前几乎最好的加密算法
            long createTokenTime = Long.parseLong(decryptStr.substring(32, 45));
            // 系统类型
            int sysType = Integer.parseInt(decryptStr.substring(45));
            // token的过期时间
            int expiresIn = getExpiresIn(sysType);
            long second = 1000L;
            if (System.currentTimeMillis() - createTokenTime > expiresIn * second) {
                throw new WillShopBindException(ResponseEnum.UNAUTHORIZED, "token error");
            }
        } catch (Exception e) {
            throw new WillShopBindException(ResponseEnum.UNAUTHORIZED, "token error");
        }

        // 防止解密后的token是脚本，从而对redis进行攻击，uuid只能是数字和小写字母
        if (!PrincipalUtil.isSimpleChar(decryptToken)) {
            throw new WillShopBindException(ResponseEnum.UNAUTHORIZED, "token error");
        }
        return decryptToken;
    }

    public String getAccessKey(String accessToken) {
        return OauthCacheNames.ACCESS + accessToken;
    }

    public String getUserIdToAccessKey(String approvalKey) {
        return OauthCacheNames.UID_TO_ACCESS + approvalKey;
    }

    public String getRefreshToAccessKey(String refreshToken) {
        return OauthCacheNames.REFRESH_TO_ACCESS + refreshToken;
    }

    public TokenInfoVO storeAndGetVo(UserInfoInTokenBO userInfoInToken) {
        TokenInfoBO tokenInfoBO = storeAccessToken(userInfoInToken);

        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        tokenInfoVO.setAccessToken(tokenInfoBO.getAccessToken());
        tokenInfoVO.setRefreshToken(tokenInfoBO.getRefreshToken());
        tokenInfoVO.setExpiresIn(tokenInfoBO.getExpiresIn());
        return tokenInfoVO;
    }

    public void deleteCurrentToken(String accessToken) {
        String decryptToken = decryptToken(accessToken);

        UserInfoInTokenBO userInfoInToken = getUserInfoByAccessToken(accessToken, true);

        String uidKey = getUserIdToAccessKey(getApprovalKey(userInfoInToken.getSysType().toString(), userInfoInToken.getUserId()));
        Long size = redisTemplate.opsForSet().size(uidKey);
        if (size == null || size == 0) {
            return;
        }
        List<String> tokenInfoBoList = stringRedisTemplate.opsForSet().pop(uidKey, size);

        if (CollUtil.isEmpty(tokenInfoBoList)) {
            return;
        }
        String dbAccessToken = null;
        String dbRefreshToken = null;
        List<byte[]> list = new ArrayList<>();
        for (String accessTokenWithRefreshToken : tokenInfoBoList) {
            String[] accessTokenWithRefreshTokenArr = accessTokenWithRefreshToken.split(StrUtil.COLON);
            dbAccessToken = accessTokenWithRefreshTokenArr[0];
            if (decryptToken.equals(dbAccessToken)) {
                dbRefreshToken = accessTokenWithRefreshTokenArr[1];
                redisTemplate.delete(getRefreshToAccessKey(dbRefreshToken));
                redisTemplate.delete(getAccessKey(dbAccessToken));
                continue;
            }
            list.add(accessTokenWithRefreshToken.getBytes(StandardCharsets.UTF_8));
        }

        if (CollUtil.isNotEmpty(list)) {
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                connection.sAdd(uidKey.getBytes(StandardCharsets.UTF_8), ArrayUtil.toArray(list, byte[].class));
                return null;
            });
        }
    }
}
