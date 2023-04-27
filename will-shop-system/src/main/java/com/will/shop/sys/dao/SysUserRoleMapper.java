package com.will.shop.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.sys.model.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 根据角色ID数组，批量删除
     * @param roleIds 多个角色ID
     * @return 影响行数
     */
    int deleteBatchByRoleId(@Param("roleIds") Long[] roleIds);

    /**
     * 根据用户ID数组，批量删除
     * @param userIds 多个用户ID
     */
    void deleteBatchByUserId(@Param("userIds") Long[] userIds);

    /**
     * 根据用户id删除用户与角色关系
     * @param userId 用户id
     */
    void deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据用户id 批量添加用户角色关系
     * @param userId 用户id
     * @param roleIdList 角色列表
     */
    void insertUserAndUserRole(@Param("userId") Long userId, @Param("roleIdList") List<Long> roleIdList);
}
