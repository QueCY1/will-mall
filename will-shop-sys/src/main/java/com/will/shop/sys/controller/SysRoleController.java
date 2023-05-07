package com.will.shop.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.common.annotation.SysLog;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.sys.model.SysRole;
import com.will.shop.sys.service.SysMenuService;
import com.will.shop.sys.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 角色管理
 *
 * @author will
 */
@RestController
@RequestMapping("/sys/role")
@Tag(name = "角色管理处理器")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    private final SysMenuService sysMenuService;

    /**
     * 角色列表
     */
    @GetMapping("/page")
    @Operation(summary = "查看角色列表", description = "查看角色列表")
    @PreAuthorize("@pm.hasPermission('sys:role:page')")
    public ServerResponseEntity<IPage<SysRole>> page(String roleName, PageParam<SysRole> page) {
        IPage<SysRole> sysRoles = sysRoleService.page(page, new LambdaQueryWrapper<SysRole>()
                .like(StrUtil.isNotBlank(roleName), SysRole::getRoleName, roleName));
        return ServerResponseEntity.success(sysRoles);
    }

    /**
     * 角色列表
     */
    @GetMapping("/list")
    @PreAuthorize("@pm.hasPermission('sys:role:list')")
    public ServerResponseEntity<List<SysRole>> list() {
        List<SysRole> list = sysRoleService.list();
        return ServerResponseEntity.success(list);
    }

    /**
     * 角色信息
     */
    @GetMapping("/info/{roleId}")
    @Operation(summary = "查看角色详情信息", description = "查看角色详情信息(编辑按钮)")
    @PreAuthorize("@pm.hasPermission('sys:role:info')")
    public ServerResponseEntity<SysRole> info(@PathVariable("roleId") Long roleId) {
        SysRole role = sysRoleService.getById(roleId);

        //查询角色对应的菜单
        List<Long> menuList = sysMenuService.listMenuIdByRoleId(roleId);
        role.setMenuIdList(menuList);

        return ServerResponseEntity.success(role);
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @PostMapping
    @Operation(summary = "保存角色信息", description = "保存角色信息")
    @PreAuthorize("@pm.hasPermission('sys:role:save')")
    public ServerResponseEntity<String> save(@RequestBody SysRole role) {
        sysRoleService.saveRoleAndRoleMenu(role);
        return ServerResponseEntity.success("保存角色信息成功！");
    }

    /**
     * 修改角色
     */
    @SysLog("修改角色")
    @PutMapping
    @Operation(summary = "修改角色信息", description = "修改角色信息")
    @PreAuthorize("@pm.hasPermission('sys:role:update')")
    public ServerResponseEntity<String> update(@RequestBody SysRole role) {
        sysRoleService.updateRoleAndRoleMenu(role);
        return ServerResponseEntity.success("修改角色信息成功！");
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @DeleteMapping
    @Operation(summary = "删除角色信息", description = "删除角色信息(删除按钮)")
    @PreAuthorize("@pm.hasPermission('sys:role:delete')")
    public ServerResponseEntity<String> delete(@RequestBody Long[] roleIds) {
        sysRoleService.deleteBatch(roleIds);
        return ServerResponseEntity.success("删除角色成功！");
    }

}
