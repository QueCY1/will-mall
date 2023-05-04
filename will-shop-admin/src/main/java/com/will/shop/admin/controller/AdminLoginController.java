package com.will.shop.admin.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.admin.dto.CaptchaAuthenticationDTO;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "账号密码 + 验证码登录（用于后台登录）", description = "通过账号/手机号/用户名密码登录")
    public ServerResponseEntity<?> login(CaptchaAuthenticationDTO captchaAuthenticationDTO) {
        // 登录后台管理系统需要再校验一遍验证码
        CaptchaVO captchaVO = new CaptchaVO();
        // 将前端传来DTO中的验证信息塞入captchaVO
        captchaVO.setCaptchaVerification(captchaAuthenticationDTO.getCaptchaVerification());
        ResponseModel verification = captchaService.verification(captchaVO);
        if (!verification.isSuccess()) {
            return ServerResponseEntity.showFailMsg("验证码有误或已过期");
        }

        return null;
    }

}
