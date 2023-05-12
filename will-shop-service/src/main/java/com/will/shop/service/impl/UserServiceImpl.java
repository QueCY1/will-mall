package com.will.shop.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.User;
import com.will.shop.bean.param.UserRegisterParam;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.util.RedisUtil;
import com.will.shop.dao.UserMapper;
import com.will.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;


    @Override
    @Cacheable(cacheNames = "user", key = "#userId")
    public User getUserByUserId(String userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 看看有没有校验验证码成功的标识
     */
    @Override
    public void validate(UserRegisterParam userRegisterParam, String checkRegisterSmsFlag) {
        if (StrUtil.isBlank(userRegisterParam.getCheckRegisterSmsFlag())) {
            // 验证码已过期，请重新发送验证码校验
            throw new WillShopBindException("验证码已过期，请重新发送验证码校验");
        } else {
            String checkRegisterSmsFlagMobile = RedisUtil.get(checkRegisterSmsFlag);
            if (!Objects.equals(checkRegisterSmsFlagMobile, userRegisterParam.getMobile())) {
                // 验证码已过期，请重新发送验证码校验
                throw new WillShopBindException("验证码已过期，请重新发送验证码校验");
            }
        }
    }
}
