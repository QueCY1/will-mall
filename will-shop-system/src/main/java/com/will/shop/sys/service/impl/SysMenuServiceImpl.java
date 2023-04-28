package com.will.shop.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.sys.dao.SysMenuMapper;
import com.will.shop.sys.dao.SysRoleMenuMapper;
import com.will.shop.sys.dao.SysUserRoleMapper;
import com.will.shop.sys.model.SysMenu;
import com.will.shop.sys.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sysMenuService")
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {


    private final SysRoleMenuMapper sysRoleMenuMapper;

    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> listMenuByUserId(Long userId) {
        sysMenuMapper
        return null;
    }

    @Override
    public void deleteMenuAndRoleMenu(Long menuId) {

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
