package com.will.shop.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.sys.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author will
 * 系统用户dao层
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> queryAllPerms(@Param("userId") Long userId);

    /**
     * 根据用户id 批量删除用户
     *
     * @param userIds 多个用户ID
     * @param shopId  商铺ID
     */
    void deleteBatch(@Param("userIds") Long[] userIds, @Param("shopId") Long shopId);

    /**
     * 根据用户名获取管理员用户
     *
     * @param username 用户名
     * @return 系统用户
     */
    SysUser selectByUsername(@Param("username") String username);

}
