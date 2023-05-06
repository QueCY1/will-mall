package com.will.shop.security.common.manager;

import cn.hutool.core.util.StrUtil;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.util.IpHelper;
import com.will.shop.common.util.RedisUtil;
import com.will.shop.security.common.enums.SysTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author will
 * @date 2023/05/06
 */
@Component
@RequiredArgsConstructor
public class PasswordCheckManager {

    private final PasswordEncoder passwordEncoder;

    /**
     * 半小时内最多错误10次
     */
    private static final int TIMES_CHECK_INPUT_PASSWORD_NUM = 10;

    /**
     * 检查用户输入错误的验证码次数
     */
    private static final String CHECK_VALID_CODE_NUM_PREFIX = "checkUserInputErrorPassword_";

    public void checkPassword(SysTypeEnum sysTypeEnum, String userNameOrMobile, String rawPassword, String encodedPassword) {

        String checkPrefix = sysTypeEnum.value() + CHECK_VALID_CODE_NUM_PREFIX + IpHelper.getIpAddr();

        int count = 0;
        if (RedisUtil.hasKey(checkPrefix + userNameOrMobile)) {
            count = RedisUtil.get(checkPrefix + userNameOrMobile);
        }
        if (count > TIMES_CHECK_INPUT_PASSWORD_NUM) {
            throw new WillShopBindException("密码输入错误十次，已限制登录30分钟");
        }
        // 半小时后失效
        RedisUtil.set(checkPrefix + userNameOrMobile, count, 1800);
        // 密码不正确
        if (StrUtil.isBlank(encodedPassword) || !passwordEncoder.matches(rawPassword, encodedPassword)) {
            count++;
            // 半小时后失效
            RedisUtil.set(checkPrefix + userNameOrMobile, count, 1800);
            throw new WillShopBindException("账号或密码不正确");
        }
    }
}
