package com.will.shop.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.admin.dto.CaptchaAuthenticationDTO;
import com.will.shop.security.common.bo.UserInfoInTokenBO;
import com.will.shop.security.common.enums.SysTypeEnum;
import com.will.shop.security.common.manager.PasswordCheckManager;
import com.will.shop.security.common.manager.PasswordManager;
import com.will.shop.security.common.manager.TokenStore;
import com.will.shop.security.common.vo.TokenInfoVO;
import com.will.shop.sys.enums.RoleEnum;
import com.will.shop.sys.model.SysMenu;
import com.will.shop.sys.model.SysUser;
import com.will.shop.sys.service.SysMenuService;
import com.will.shop.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Tag(name = "后台用户登录")
@RequiredArgsConstructor
public class AdminLoginController {

    private final TokenStore tokenStore;

    private final CaptchaService captchaService;

    private final SysUserService sysUserService;

    private final SysMenuService sysMenuService;

    private final PasswordCheckManager passwordCheckManager;

    private final PasswordManager passwordManager;

    @PostMapping("/adminLogin")
    @Operation(summary = "账号密码 + 验证码登录（用于后台登录）", description = "通过账号/手机号/用户名密码登录")
    public ServerResponseEntity<?> login(@Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
          // 登录后台管理系统需要再校验一遍验证码
//        CaptchaVO captchaVO = new CaptchaVO();
//        // 将前端传来DTO中的验证信息塞入captchaVO
//        captchaVO.setCaptchaVerification(captchaAuthenticationDTO.getCaptchaVerification());
//        ResponseModel verification = captchaService.verification(captchaVO);
//        if (!verification.isSuccess()) {
//            return ServerResponseEntity.showFailMsg("验证码有误或已过期");
//        }

        SysUser sysUser = sysUserService.getByUserName(captchaAuthenticationDTO.getUserName());
        if (sysUser == null) {
            throw new WillShopBindException("账号或密码不正确");
        }

        // 半小时内密码输入错误十次，已限制登录30分钟
//        String decryptPassword = passwordManager.decryptPassword(captchaAuthenticationDTO.getPassWord());
//        passwordCheckManager.checkPassword(SysTypeEnum.ADMIN,captchaAuthenticationDTO.getUserName(), decryptPassword, sysUser.getPassword());

        // 不是店铺超级管理员，并且是禁用状态，无法登录
        if (Objects.equals(sysUser.getStatus(), 0)) {
            // 未找到此用户信息
            throw new WillShopBindException("未找到此用户信息");
        }

        UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();
        userInfoInToken.setUserId(String.valueOf(sysUser.getUserId()));
        userInfoInToken.setSysType(SysTypeEnum.ADMIN.value());
        userInfoInToken.setEnabled(sysUser.getStatus() == 1);
        userInfoInToken.setPerms(getUserPermissions(sysUser.getUserId()));
        userInfoInToken.setNickName(sysUser.getUsername());
        userInfoInToken.setShopId(sysUser.getShopId());
        // 存储token返回vo
        TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(userInfoInToken);
        return ServerResponseEntity.success(tokenInfoVO);
    }

    private Set<String> getUserPermissions(Long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == RoleEnum.SUPER_ADMIN.value()) {
            List<SysMenu> menuList = sysMenuService.list(Wrappers.emptyWrapper());
            permsList = menuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
        } else {
            permsList = sysUserService.queryAllPerms(userId);
        }
        return permsList.stream().flatMap((perms) -> {
            if (StrUtil.isBlank(perms)) {
                return null;
            }
            return Arrays.stream(perms.trim().split(StrUtil.COMMA));
        }).collect(Collectors.toSet());
    }

}
