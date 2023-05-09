package com.will.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.bean.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author will
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户邮箱获取用户信息
     */
    User getUserByUserMail(@Param("userMail") String userMail);

    /**
     * 根据用户名称获取一个用户信息
     */
    User selectOneByUserName(@Param("userName") String userName);
}
