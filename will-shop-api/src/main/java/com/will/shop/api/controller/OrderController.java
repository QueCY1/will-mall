package com.will.shop.api.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.will.shop.bean.app.dto.*;
import com.will.shop.bean.app.param.OrderParam;
import com.will.shop.bean.app.param.OrderShopParam;
import com.will.shop.bean.app.param.SubmitOrderParam;
import com.will.shop.bean.event.ConfirmOrderEvent;
import com.will.shop.bean.model.Order;
import com.will.shop.bean.model.UserAddr;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.Arithmetic;
import com.will.shop.security.api.util.SecurityUtils;
import com.will.shop.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author will
 */
@RestController
@RequestMapping("/p/order")
@Tag(name = "订单接口")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final SkuService skuService;

    private final ProductService productService;

    private final UserAddrService userAddrService;

    private final BasketService basketService;

    private final ApplicationContext applicationContext;


    /**
     * 生成订单，直接购买或者统一下单购买
     */
    @PostMapping("/confirm")
    @Operation(summary = "结算，生成订单信息", description = "传入下单所需要的参数进行下单")
    public ServerResponseEntity<ShopCartOrderMergerDto> confirm(@Valid @RequestBody OrderParam orderParam) {
        String userId = SecurityUtils.getUser().getUserId();

        // 订单的地址信息
        UserAddr userAddr = userAddrService.getUserAddrByUserId(orderParam.getAddrId(), userId);
        UserAddrDto userAddrDto = BeanUtil.copyProperties(userAddr, UserAddrDto.class);


        // 组装获取用户提交的购物车商品项
        List<ShopCartItemDto> shopCartItems = basketService.getShopCartItemsByOrderItems(orderParam.getBasketIds(), orderParam.getOrderItem(), userId);

        if (CollectionUtil.isEmpty(shopCartItems)) {
            throw new WillShopBindException("请选择您需要的商品加入购物车");
        }

        // 根据店铺组装购车中的商品信息，返回每个店铺中的购物车商品信息
        List<ShopCartDto> shopCarts = basketService.getShopCarts(shopCartItems);

        // 将要返回给前端的完整的订单信息
        ShopCartOrderMergerDto shopCartOrderMergerDto = new ShopCartOrderMergerDto();

        shopCartOrderMergerDto.setUserAddr(userAddrDto);

        // 所有店铺的订单信息
        List<ShopCartOrderDto> shopCartOrders = new ArrayList<>();

        double actualTotal = 0.0;
        double total = 0.0;
        int totalCount = 0;
        double orderReduce = 0.0;
        for (ShopCartDto shopCart : shopCarts) {

            // 每个店铺的订单信息
            ShopCartOrderDto shopCartOrder = new ShopCartOrderDto();
            shopCartOrder.setShopId(shopCart.getShopId());
            shopCartOrder.setShopName(shopCart.getShopName());


            List<ShopCartItemDiscountDto> shopCartItemDiscounts = shopCart.getShopCartItemDiscounts();

            // 店铺中的所有商品项信息
            List<ShopCartItemDto> shopAllShopCartItems = new ArrayList<>();
            for (ShopCartItemDiscountDto shopCartItemDiscount : shopCartItemDiscounts) {
                List<ShopCartItemDto> discountShopCartItems = shopCartItemDiscount.getShopCartItems();
                shopAllShopCartItems.addAll(discountShopCartItems);
            }

            shopCartOrder.setShopCartItemDiscounts(shopCartItemDiscounts);

            applicationContext.publishEvent(new ConfirmOrderEvent(shopCartOrder, orderParam, shopAllShopCartItems));

            actualTotal = Arithmetic.add(actualTotal, shopCartOrder.getActualTotal());
            total = Arithmetic.add(total, shopCartOrder.getTotal());
            totalCount = totalCount + shopCartOrder.getTotalCount();
            orderReduce = Arithmetic.add(orderReduce, shopCartOrder.getShopReduce());
            shopCartOrders.add(shopCartOrder);


        }

        shopCartOrderMergerDto.setActualTotal(actualTotal);
        shopCartOrderMergerDto.setTotal(total);
        shopCartOrderMergerDto.setTotalCount(totalCount);
        shopCartOrderMergerDto.setShopCartOrders(shopCartOrders);
        shopCartOrderMergerDto.setOrderReduce(orderReduce);

        shopCartOrderMergerDto = orderService.putConfirmOrderCache(userId, shopCartOrderMergerDto);

        return ServerResponseEntity.success(shopCartOrderMergerDto);
    }

    /**
     * 购物车/立即购买  提交订单,根据店铺拆单
     */
    @PostMapping("/submit")
    @Operation(summary = "提交订单，返回支付流水号", description = "根据传入的参数判断是否为购物车提交订单，同时对购物车进行删除，用户开始进行支付")
    public ServerResponseEntity<OrderNumbersDto> submitOrders(@Valid @RequestBody SubmitOrderParam submitOrderParam) {
        String userId = SecurityUtils.getUser().getUserId();
        ShopCartOrderMergerDto mergerOrder = orderService.getConfirmOrderCache(userId);
        if (mergerOrder == null) {
            throw new WillShopBindException("订单已过期，请重新下单");
        }

        List<OrderShopParam> orderShopParams = submitOrderParam.getOrderShopParam();

        List<ShopCartOrderDto> shopCartOrders = mergerOrder.getShopCartOrders();
        // 设置备注
        if (CollectionUtil.isNotEmpty(orderShopParams)) {
            for (ShopCartOrderDto shopCartOrder : shopCartOrders) {
                for (OrderShopParam orderShopParam : orderShopParams) {
                    if (Objects.equals(shopCartOrder.getShopId(), orderShopParam.getShopId())) {
                        shopCartOrder.setRemarks(orderShopParam.getRemarks());
                    }
                }
            }
        }

        List<Order> orders = orderService.submit(userId, mergerOrder);

        StringBuilder orderNumbers = new StringBuilder();
        for (Order order : orders) {
            orderNumbers.append(order.getOrderNumber()).append(",");
        }
        orderNumbers.deleteCharAt(orderNumbers.length() - 1);

        boolean isShopCartOrder = false;
        // 移除缓存
        for (ShopCartOrderDto shopCartOrder : shopCartOrders) {
            for (ShopCartItemDiscountDto shopCartItemDiscount : shopCartOrder.getShopCartItemDiscounts()) {
                for (ShopCartItemDto shopCartItem : shopCartItemDiscount.getShopCartItems()) {
                    Long basketId = shopCartItem.getBasketId();
                    if (basketId != null && basketId != 0) {
                        isShopCartOrder = true;
                    }
                    skuService.removeSkuCacheBySkuId(shopCartItem.getSkuId(), shopCartItem.getProdId());
                    productService.removeProductCacheByProdId(shopCartItem.getProdId());
                }
            }
        }
        // 购物车提交订单时(即有购物车ID时)
        if (isShopCartOrder) {
            basketService.removeShopCartItemsCacheByUserId(userId);
        }
        orderService.removeConfirmOrderCache(userId);
        return ServerResponseEntity.success(new OrderNumbersDto(orderNumbers.toString()));
    }

}
