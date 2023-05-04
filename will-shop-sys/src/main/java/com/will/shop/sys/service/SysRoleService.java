package com.will.shop.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.sys.model.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据角色id批量删除：比如把所有管理员删除
     * @param roleIds 多个角色id
     */
    void deleteBatch(Long[] roleIds);

    /**
     * 保存角色 与 角色菜单关系
     * @param role 系统角色
     */
    void saveRoleAndRoleMenu(SysRole role);

    /**
     * 更新角色 与 角色菜单关系
     * @param role 系统角色
     */
    void updateRoleAndRoleMenu(SysRole role);

    /**
     * 根据用户ID，获取角色ID列表，一个用户对应多个角色，每个角色对应了不同的权限
     * @param userId 用户id
     * @return 角色id列表
     */
    List<Long> listRoleIdByUserId(Long userId);

}
