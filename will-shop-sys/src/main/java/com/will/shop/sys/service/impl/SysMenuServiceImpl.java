package com.will.shop.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.sys.dao.SysMenuMapper;
import com.will.shop.sys.dao.SysRoleMenuMapper;
import com.will.shop.sys.model.SysMenu;
import com.will.shop.sys.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.will.shop.sys.enums.RoleEnum.ADMIN;

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
        if (userId == ADMIN.value()) {
            sysMenus = sysMenuMapper.listMenu();
        } else {
            sysMenus = sysMenuMapper.listMenuByUserId(userId);
        }

        Map<Long, List<SysMenu>> sysMenuLevelMap = sysMenus.stream()
                .sorted(Comparator.comparing(SysMenu::getOrderNum))
                .collect(Collectors.groupingBy(SysMenu::getParentId));

        //展示嵌套关系
        // 一级菜单
        List<SysMenu> rootMenu = sysMenuLevelMap.get(0L);
        if (CollectionUtil.isEmpty(rootMenu)) {
            return Collections.emptyList();
        }
        // 二级菜单
        for (SysMenu sysMenu : rootMenu) {
            sysMenu.setList(sysMenuLevelMap.get(sysMenu.getMenuId()));
        }
        return rootMenu;
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
        return sysMenuMapper.listMenuIdByRoleId(roleId);
    }

    @Override
    public List<SysMenu> listSimpleMenuNoButton() {
        return sysMenuMapper.listSimpleMenuNoButton();
    }

    @Override
    public List<SysMenu> listRootMenu() {
        return sysMenuMapper.listRootMenu();
    }

    @Override
    public List<SysMenu> listChildrenMenuByParentId(Long parentId) {
        return sysMenuMapper.listChildrenMenuByParentId(parentId);
    }

    @Override
    public List<SysMenu> listMenuAndButton() {
        return sysMenuMapper.listMenuAndBtn();
    }
}
