package com.will.shop.sys.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.common.annotation.SysLog;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.security.common.enums.SysTypeEnum;
import com.will.shop.security.common.manager.PasswordManager;
import com.will.shop.security.common.manager.TokenStore;
import com.will.shop.sys.dto.UpdatePasswordDTO;
import com.will.shop.sys.enums.RoleEnum;
import com.will.shop.sys.model.SysUser;
import com.will.shop.sys.service.SysRoleService;
import com.will.shop.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/sys/user")
@Tag(name = "管理员列表处理器")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    private final SysRoleService sysRoleService;

    private final PasswordEncoder passwordEncoder;

    private final PasswordManager passwordManager;

    private final TokenStore tokenStore;

    /**
     * 获取所有用户列表并翻页(包含搜索功能)
     *
     * @param username 用户名
     * @param page     当前页数
     * @return 响应数据
     */
    @GetMapping("/page")
    @Operation(summary = "获取所有用户列表", description = "获取所有用户列表并翻页(包含搜索功能)")
    @PreAuthorize("@pm.hasPermission('sys:user:page')")
    public ServerResponseEntity<IPage<SysUser>> page(String username, PageParam<SysUser> page) {
        IPage<SysUser> sysUserPage = sysUserService.page(page, new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getShopId, SecurityUtils.getSysUser().getShopId())
                .like(StrUtil.isNotBlank(username), SysUser::getUsername, username));
        return ServerResponseEntity.success(sysUserPage);
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取登录用户信息", description = "获取登录的用户信息")
    public ServerResponseEntity<SysUser> info() {
        return ServerResponseEntity.success(sysUserService.getSysUserById(SecurityUtils.getSysUser().getUserId()));
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @PostMapping("/password")
    @Operation(summary = "修改密码", description = "修改当前登陆用户的密码(前端右上角位置)")
    public ServerResponseEntity<String> password(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        //去userRuntime里面拿用户的id
        Long userId = SecurityUtils.getSysUser().getUserId();

        //admin账号密码不可修改
        if (Objects.equals(userId, 1L) && StrUtil.isNotBlank(updatePasswordDTO.getNewPassword())) {
            throw new WillShopBindException("禁止修改admin的账号密码");
        }
        SysUser dbSysUser = sysUserService.getSysUserById(userId);
        String password = passwordManager.decryptPassword(updatePasswordDTO.getPassword());
        //判断原密码是否正确
        if (!passwordEncoder.matches(password, dbSysUser.getPassword())) {
            return ServerResponseEntity.showFailMsg("原密码不正确！");
        }
        //新密码
        String newPassword = passwordEncoder.encode(passwordManager.decryptPassword(updatePasswordDTO.getNewPassword()));
        //更新密码
        sysUserService.updatePasswordByUserId(userId, newPassword);
        //清空原账号的所有token
        tokenStore.deleteAllToken(String.valueOf(SysTypeEnum.ADMIN.value()), String.valueOf(userId));

        return ServerResponseEntity.success("密码更改成功！");
    }

    /**
     * 用户信息
     */
    @GetMapping("/info/{userId}")
    @Operation(summary = "获取某用户信息", description = "获取某个用户信息(管理员列表下点击用户的编辑按钮)")
    @PreAuthorize("@pm.hasPermission('sys:user:info')")
    public ServerResponseEntity<SysUser> info(@PathVariable("userId") Long userId) {
        SysUser user = sysUserService.getSysUserById(userId);
        user.setUserId(null);
        //商户id在超级管理员创建之后就已经存在，每个企业使用该平台对应一个shopId
        //这个实际上在数据库上就已经查不出来了，这里是做进一步校验
        if (!Objects.equals(user.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            throw new WillShopBindException("没有权限获取该用户信息");
        }
        //获取用户所属的角色列表
        List<Long> roleIdList = sysRoleService.listRoleIdByUserId(userId);
        user.setRoleIdList(roleIdList);
        return ServerResponseEntity.success(user);
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @PutMapping
    @Operation(summary = "修改用户信息", description = "修改用户信息")
    @PreAuthorize("@pm.hasPermission('sys:user:update')")
    public ServerResponseEntity<String> update(@Valid @RequestBody SysUser user) {
        String password = passwordManager.decryptPassword(user.getPassword());
        SysUser dbUser = sysUserService.getSysUserById(user.getUserId());

        if (!Objects.equals(dbUser.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            throw new WillShopBindException("没有权限修改该用户信息");
        }
        SysUser dbUserInSameName = sysUserService.getByUserName(user.getUsername());

        if (dbUserInSameName != null && !Objects.equals(dbUserInSameName.getUserId(), user.getUserId())) {
            return ServerResponseEntity.showFailMsg("该用户名已存在");
        }
        if (StrUtil.isBlank(password)) {
            user.setPassword(null);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        // 开源版代码，禁止用户修改admin 的账号密码密码
        // 正式使用时，删除此部分代码即可
        boolean is = Objects.equals(1L, dbUser.getUserId()) && (StrUtil.isNotBlank(password) || !StrUtil.equals("admin", user.getUsername()));
        if (is) {
            throw new WillShopBindException("禁止修改admin的账号密码");
        }

        if (Objects.equals(1L, user.getUserId()) && user.getStatus() == 0) {
            throw new WillShopBindException("admin用户不可以被禁用");
        }
        sysUserService.updateUserAndUserRole(user);
        return ServerResponseEntity.success("修改用户成功！");
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @DeleteMapping
    @Operation(summary = "删除用户信息", description = "删除用户信息")
    @PreAuthorize("@pm.hasPermission('sys:user:delete')")
    public ServerResponseEntity<String> delete(@RequestBody Long[] userIds) {
        if (userIds.length == 0) {
            return ServerResponseEntity.showFailMsg("请选择需要删除的用户");
        }
        if (ArrayUtil.contains(userIds, RoleEnum.SUPER_ADMIN.value())) {
            return ServerResponseEntity.showFailMsg("系统管理员不能删除");
        }
        if (ArrayUtil.contains(userIds, SecurityUtils.getSysUser().getUserId())) {
            return ServerResponseEntity.showFailMsg("当前用户不能删除");
        }
        //删除该企业下该用户的信息
        sysUserService.deleteBatch(userIds, SecurityUtils.getSysUser().getShopId());
        return ServerResponseEntity.success("删除用户成功!");
    }

}
