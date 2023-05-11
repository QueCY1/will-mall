package com.will.shop.security.api.util;

import com.will.shop.security.api.model.WillUser;
import com.will.shop.common.util.HttpContextUtils;
import com.will.shop.security.common.bo.UserInfoInTokenBO;
import com.will.shop.security.common.util.AuthUserContext;
import lombok.experimental.UtilityClass;

/**
 * @author will
 */
@UtilityClass
public class SecurityUtils {

    private static final String USER_REQUEST = "/p/";

    /**
     * 获取用户
     */
    public WillUser getUser() {
        if (!HttpContextUtils.getHttpServletRequest().getRequestURI().startsWith(USER_REQUEST)) {
            // 用户相关的请求，应该以/p开头！！！
            throw new RuntimeException("will.user.request.error");
        }
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();

        WillUser willUser = new WillUser();
        willUser.setUserId(userInfoInTokenBO.getUserId());
        willUser.setBizUserId(userInfoInTokenBO.getBizUserId());
        willUser.setEnabled(userInfoInTokenBO.getEnabled());
        willUser.setShopId(userInfoInTokenBO.getShopId());
        willUser.setStationId(userInfoInTokenBO.getOtherId());
        return willUser;
    }
}
