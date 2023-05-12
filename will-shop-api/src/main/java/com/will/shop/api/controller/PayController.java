package com.will.shop.api.controller;

import com.will.shop.bean.app.param.PayParam;
import com.will.shop.bean.app.pay.PayInfoDto;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.api.model.WillUser;
import com.will.shop.security.api.util.SecurityUtils;
import com.will.shop.service.PayService;
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
@RequestMapping("/p/order")
@Tag(name = "订单接口")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    /**
     * 支付接口
     */
    @PostMapping("/pay")
    @Operation(summary = "根据订单号进行支付", description = "根据订单号进行支付")
    public ServerResponseEntity<Void> pay(@RequestBody PayParam payParam) {
        WillUser user = SecurityUtils.getUser();
        String userId = user.getUserId();
        PayInfoDto payInfo = payService.pay(userId, payParam);
        payService.paySuccess(payInfo.getPayNo(), "");
        return ServerResponseEntity.success();
    }

    /**
     * 普通支付接口
     */
    @PostMapping("/normalPay")
    @Operation(summary = "根据订单号进行支付", description = "根据订单号进行支付")
    public ServerResponseEntity<Boolean> normalPay(@RequestBody PayParam payParam) {

        WillUser user = SecurityUtils.getUser();
        String userId = user.getUserId();
        PayInfoDto payInfo = payService.pay(userId, payParam);

        // 根据内部订单号更新order settlement
        payService.paySuccess(payInfo.getPayNo(), "");

        return ServerResponseEntity.success(true);
    }
}
