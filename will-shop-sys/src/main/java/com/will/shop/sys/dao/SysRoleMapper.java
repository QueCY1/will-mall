package com.will.shop.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.sys.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据角色id批量删除：比如把所有管理员删除
     * @param roleIds 多个角色id
     */
    void deleteBatch(@Param("roleIds") Long[] roleIds);

    /**
     * 根据用户ID，获取角色ID列表，一个用户对应多个角色，每个角色对应了不同的权限
     * @param userId 用户id
     * @return 角色id列表
     */
    List<Long> listRoleIdByUserId(@Param("userId") Long userId);
}
