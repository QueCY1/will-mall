package com.will.shop.api.controller;

import com.google.common.collect.Maps;
import com.will.shop.bean.app.param.SendSmsParam;
import com.will.shop.bean.enums.SmsType;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.api.util.SecurityUtils;
import com.will.shop.service.SmsLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author will
 */
@RestController
@RequestMapping("/p/sms")
@Tag(name = "发送验证码接口")
@RequiredArgsConstructor
public class SmsController {

    private final SmsLogService smsLogService;

    /**
     * 发送验证码接口
     */
    @PostMapping("/send")
    @Operation(summary = "发送验证码", description = "用户的发送验证码")
    public ServerResponseEntity<Void> audit(@RequestBody SendSmsParam sendSmsParam) {
        String userId = SecurityUtils.getUser().getUserId();
        smsLogService.sendSms(SmsType.VALID, userId, sendSmsParam.getMobile(), Maps.newHashMap());

        return ServerResponseEntity.success();
    }
}
