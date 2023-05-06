package com.will.shop.sys.controller;

import com.will.shop.security.common.manager.PasswordManager;
import com.will.shop.security.common.manager.TokenStore;
import com.will.shop.sys.service.SysRoleService;
import com.will.shop.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    private final SysRoleService sysRoleService;

    private final PasswordEncoder passwordEncoder;

    private final PasswordManager passwordManager;

    private final TokenStore tokenStore;


}
