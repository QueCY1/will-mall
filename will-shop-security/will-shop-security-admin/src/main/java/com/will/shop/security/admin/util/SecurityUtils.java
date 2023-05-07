/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.will.shop.security.admin.util;

import com.will.shop.security.admin.model.WillSysUser;
import com.will.shop.security.common.bo.UserInfoInTokenBO;
import com.will.shop.security.common.util.AuthUserContext;
import lombok.experimental.UtilityClass;

/**
 * @author LGH
 */
@UtilityClass
public class SecurityUtils {
    /**
     * 获取用户
     */
    public WillSysUser getSysUser() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();

        WillSysUser sysUser = new WillSysUser();
        sysUser.setUserId(Long.valueOf(userInfoInTokenBO.getUserId()));
        sysUser.setEnabled(userInfoInTokenBO.getEnabled());
        sysUser.setUsername(userInfoInTokenBO.getNickName());
        sysUser.setAuthorities(userInfoInTokenBO.getPerms());
        sysUser.setShopId(userInfoInTokenBO.getShopId());
        return sysUser;
    }
}

