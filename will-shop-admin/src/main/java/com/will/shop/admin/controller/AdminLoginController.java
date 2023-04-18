package com.will.shop.admin.controller;

import com.anji.captcha.service.CaptchaService;
import com.will.shop.security.admin.dto.CaptchaAuthenticationDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "后台用户登录")
public class AdminLoginController {

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/adminLogin")
    public String login(CaptchaAuthenticationDTO captchaAuthenticationDTO) {
        return null;
    }
}
