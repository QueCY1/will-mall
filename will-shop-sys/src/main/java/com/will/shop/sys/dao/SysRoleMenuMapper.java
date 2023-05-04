package com.will.shop.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.sys.model.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 根据角色ID数组，批量删除
     * @param roleIds 角色ID数组
     * @return 删除的数量
     */
    int deleteBatch(@Param("roleIds") Long[] roleIds);

    /**
     * 根据菜单id 删除菜单关联角色信息
     * @param menuId 菜单id
     */
    void deleteByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据角色id 批量添加角色与菜单关系
     * @param roleId 角色id
     * @param menuIdList 菜单id列表
     */
    void insertRoleAndRoleMenu(@Param("roleId") Long roleId, @Param("menuIdList") List<Long> menuIdList);

}
