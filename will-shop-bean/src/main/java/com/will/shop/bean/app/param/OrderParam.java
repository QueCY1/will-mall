package com.will.shop.bean.app.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author will
 */
@Data
@Schema(description = "订单参数")
public class OrderParam {

    @Schema(description = "购物车id 数组")
    private List<Long> basketIds;

    @Schema(description = "立即购买时提交的商品项")
    private OrderItemParam orderItem;

    @Schema(description = "地址ID，0为默认地址", required = true)
    @NotNull(message = "地址不能为空")
    private Long addrId;

    @Schema(description = "用户是否改变了优惠券的选择，如果用户改变了优惠券的选择，则完全根据传入参数进行优惠券的选择")
    private Integer userChangeCoupon;

    @Schema(description = "优惠券id数组")
    private List<Long> couponIds;

//	@Schema(description = "每次订单提交时的uuid" )
//	private String uuid;
//	@Schema(description = "订单入口 SHOP_CART购物车，BUY_NOW立即购买" )
//	private OrderEntry orderEntry;


}
