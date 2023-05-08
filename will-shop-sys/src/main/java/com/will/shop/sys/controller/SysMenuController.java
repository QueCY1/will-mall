/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.will.shop.sys.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.will.shop.common.annotation.SysLog;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.sys.constant.Constant;
import com.will.shop.sys.enums.MenuEnum;
import com.will.shop.sys.model.SysMenu;
import com.will.shop.sys.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 系统菜单
 *
 * @author lgh
 */
@RestController
@RequestMapping("/sys/menu")
@Tag(name = "菜单管理处理器")
@AllArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;


    /**
     * 登陆时，获取用户所拥有的菜单和权限
     */
    @GetMapping("/nav")
    @Operation(summary = "获取用户所拥有的菜单和权限", description = "通过登陆用户的userId获取用户所拥有的菜单和权限(登陆时)")
    public ServerResponseEntity<Map<Object, Object>> nav() {
        List<SysMenu> menuList = sysMenuService.listMenuByUserId(SecurityUtils.getSysUser().getUserId());

        return ServerResponseEntity.success(MapUtil.builder()
                .put("authorities", SecurityUtils.getSysUser().getAuthorities())
                .put("menuList", menuList).build());
    }

    /**
     * 获取菜单页面的表(考虑是否需要懒加载)
     */
    @GetMapping("/table")
    @Operation(summary = "获取菜单页面的表", description = "获取菜单页面的表")
    public ServerResponseEntity<List<SysMenu>> table() {
        List<SysMenu> sysMenuList = sysMenuService.listMenuAndButton();
        return ServerResponseEntity.success(sysMenuList);
    }

    /**
     * 所有菜单列表(用于新建、修改菜单中一项时 获取菜单的信息)
     */
    @GetMapping("/list")
    @Operation(summary = "获取非按钮菜单", description = "通过新建、修改菜单中一项时 获取菜单的信息")
    public ServerResponseEntity<List<SysMenu>> list() {
        List<SysMenu> sysMenuList = sysMenuService.listSimpleMenuNoButton();
        return ServerResponseEntity.success(sysMenuList);
    }

    /**
     * 选择菜单
     */
    @GetMapping("/listRootMenu")
    @Operation(summary = "获取一级菜单", description = "获取一级菜单")
    public ServerResponseEntity<List<SysMenu>> listRootMenu() {
        //查询列表数据
        List<SysMenu> menuList = sysMenuService.listRootMenu();

        return ServerResponseEntity.success(menuList);
    }

    /**
     * 选择子菜单
     */
    @GetMapping("/listChildrenMenu")
    @Operation(summary = "选择子菜单", description = "选择子菜单")
    public ServerResponseEntity<List<SysMenu>> listChildrenMenu(Long parentId) {
        //查询列表数据
        List<SysMenu> menuList = sysMenuService.listChildrenMenuByParentId(parentId);

        return ServerResponseEntity.success(menuList);
    }

    /**
     * 菜单信息
     */
    @GetMapping("/info/{menuId}")
    @Operation(summary = "菜单信息", description = "菜单信息")
    @PreAuthorize("@pm.hasPermission('sys:menu:info')")
    public ServerResponseEntity<SysMenu> info(@PathVariable("menuId") Long menuId) {
        SysMenu menu = sysMenuService.getById(menuId);
        return ServerResponseEntity.success(menu);
    }

    /**
     * 保存
     */
    @SysLog("保存菜单")
    @PostMapping
    @PreAuthorize("@pm.hasPermission('sys:menu:save')")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SysMenu menu) {
        //数据校验
        verifyForm(menu);
        sysMenuService.save(menu);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @SysLog("修改菜单")
    @PutMapping
    @PreAuthorize("@pm.hasPermission('sys:menu:update')")
    public ServerResponseEntity<String> update(@Valid @RequestBody SysMenu menu) {
        //数据校验
        verifyForm(menu);

        if (menu.getType() == MenuEnum.MENU.getValue()) {
            if (StrUtil.isBlank(menu.getUrl())) {
                return ServerResponseEntity.showFailMsg("菜单URL不能为空");
            }
        }
        sysMenuService.updateById(menu);

        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @SysLog("删除菜单")
    @DeleteMapping("/{menuId}")
    @PreAuthorize("@pm.hasPermission('sys:menu:delete')")
    public ServerResponseEntity<String> delete(@PathVariable Long menuId) {
        if (menuId <= Constant.SYS_MENU_MAX_ID) {
            return ServerResponseEntity.showFailMsg("系统菜单，不能删除");
        }
        //判断是否有子菜单或按钮
        List<SysMenu> menuList = sysMenuService.listChildrenMenuByParentId(menuId);
        if (menuList.size() > 0) {
            return ServerResponseEntity.showFailMsg("请先删除子菜单或按钮");
        }

        sysMenuService.deleteMenuAndRoleMenu(menuId);

        return ServerResponseEntity.success();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenu menu) {

        if (menu.getType() == MenuEnum.MENU.getValue()) {
            if (StrUtil.isBlank(menu.getUrl())) {
                throw new WillShopBindException("菜单URL不能为空");
            }
        }
        if (Objects.equals(menu.getMenuId(), menu.getParentId())) {
            throw new WillShopBindException("自己不能是自己的上级");
        }

        //上级菜单类型
        int parentType = MenuEnum.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == MenuEnum.CATALOG.getValue() ||
                menu.getType() == MenuEnum.MENU.getValue()) {
            if (parentType != MenuEnum.CATALOG.getValue()) {
                throw new WillShopBindException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == MenuEnum.BUTTON.getValue()) {
            if (parentType != MenuEnum.MENU.getValue()) {
                throw new WillShopBindException("上级菜单只能为菜单类型");
            }
        }
    }
}
