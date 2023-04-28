package com.will.shop.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.sys.model.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据角色id获取菜单列表
     * @param roleId 角色id
     * @return 菜单id列表
     */
    //List<Long> listMenuIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询用户的所有菜单ID
     * @param userId 用户id
     * @return 该用户所有可用的菜单
     */
    List<SysMenu> listMenuByUserId(@Param("userId") Long userId);
}
