package com.will.shop.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.sys.dao.SysMenuMapper;
import com.will.shop.sys.dao.SysRoleMenuMapper;
import com.will.shop.sys.model.SysMenu;
import com.will.shop.sys.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("sysMenuService")
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> listMenuByUserId(Long userId) {
        //根据用户的角色，来优化返回的速度
        List<SysMenu> sysMenus;
        //超级管理员
//        if (userId == ADMIN.getValue()) {
//            return null;
//        }
        return sysMenuMapper.listMenuByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenuAndRoleMenu(Long menuId) {
        //删除菜单
        sysMenuMapper.deleteById(menuId);
        //删除菜单与角色关联表
        sysRoleMenuMapper.deleteByMenuId(menuId);
    }

    @Override
    public List<Long> listMenuIdByRoleId(Long roleId) {
        return null;
    }

    @Override
    public List<SysMenu> listSimpleMenuNoButton() {
        return null;
    }

    @Override
    public List<SysMenu> listRootMenu() {
        return null;
    }

    @Override
    public List<SysMenu> listChildrenMenuByParentId(Long parentId) {
        return null;
    }

    @Override
    public List<SysMenu> listMenuAndButton() {
        return null;
    }
}
