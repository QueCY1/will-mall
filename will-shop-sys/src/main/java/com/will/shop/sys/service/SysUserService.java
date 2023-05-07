package com.will.shop.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.sys.model.SysUser;

import java.util.List;

/**
 * @author will
 * 系统用户业务层
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     */
    void updatePasswordByUserId(Long userId, String newPassword);

    /**
     * 保存用户与用户角色关系
     *
     * @param user 系统用户
     */
    void saveUserAndUserRole(SysUser user);


    /**
     * 更新用户与用户角色关系
     *
     * @param user 系统用户
     */
    void updateUserAndUserRole(SysUser user);

    /**
     * 根据用户id 批量删除用户
     *
     * @param userIds 多个用户id
     * @param shopId  某商铺id
     */
    void deleteBatch(Long[] userIds, Long shopId);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 返回系统用户
     */
    SysUser getByUserName(String username);

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return 返回系统用户类
     */
    SysUser getSysUserById(Long userId);

    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     * @return 系统用户拥有的所有权限
     */
    List<String> queryAllPerms(Long userId);

}
