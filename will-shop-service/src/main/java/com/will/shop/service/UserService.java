package com.will.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.bean.model.User;
import com.will.shop.bean.admin.param.UserRegisterParam;

/**
 * @author will
 */
public interface UserService extends IService<User> {
    /**
     * 根据用户id获取用户信息
     */
    User getUserByUserId(String userId);

    /**
     * 校验验证码
     */
    void validate(UserRegisterParam userRegisterParam, String checkRegisterSmsFlag);
}
