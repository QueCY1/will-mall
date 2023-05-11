package com.will.shop.api.controller;

import cn.hutool.http.HttpUtil;
import com.will.shop.bean.app.dto.DeliveryDto;
import com.will.shop.bean.model.Delivery;
import com.will.shop.bean.model.Order;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.Json;
import com.will.shop.service.DeliveryService;
import com.will.shop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author will
 */
@RestController
@RequestMapping("/delivery")
@Tag(name = "查看物流接口")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    private final OrderService orderService;

    /**
     * 查看物流接口
     */
    @GetMapping("/check")
    @Operation(summary = "查看物流", description = "根据订单号查看物流")
    @Parameter(name = "orderNumber", description = "订单号", required = true)
    public ServerResponseEntity<DeliveryDto> checkDelivery(String orderNumber) {

        Order order = orderService.getOrderByOrderNumber(orderNumber);
        Delivery delivery = deliveryService.getById(order.getDvyId());
        String url = delivery.getQueryUrl().replace("{dvyFlowId}", order.getDvyFlowId());
        String deliveryJson = HttpUtil.get(url);

        DeliveryDto deliveryDto = Json.parseObject(deliveryJson, DeliveryDto.class);
        deliveryDto.setDvyFlowId(order.getDvyFlowId());
        deliveryDto.setCompanyHomeUrl(delivery.getCompanyHomeUrl());
        deliveryDto.setCompanyName(delivery.getDvyName());
        return ServerResponseEntity.success(deliveryDto);
    }
}
