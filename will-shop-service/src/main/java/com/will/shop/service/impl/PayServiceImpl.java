package com.will.shop.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.will.shop.bean.app.param.PayParam;
import com.will.shop.bean.enums.PayType;
import com.will.shop.bean.event.PaySuccessOrderEvent;
import com.will.shop.bean.model.Order;
import com.will.shop.bean.model.OrderSettlement;
import com.will.shop.bean.app.pay.PayInfoDto;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.util.Arithmetic;
import com.will.shop.dao.OrderMapper;
import com.will.shop.dao.OrderSettlementMapper;
import com.will.shop.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    private final OrderMapper orderMapper;

    private final OrderSettlementMapper orderSettlementMapper;

    private final ApplicationEventPublisher eventPublisher;

    private final Snowflake snowflake;

    /**
     * 不同的订单号，同一个支付流水号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayInfoDto pay(String userId, PayParam payParam) {


        // 不同的订单号的产品名称
        StringBuilder prodName = new StringBuilder();
        // 支付单号
        String payNo = String.valueOf(snowflake.nextId());
        String[] orderNumbers = payParam.getOrderNumbers().split(StrUtil.COMMA);
        // 修改订单信息
        for (String orderNumber : orderNumbers) {
            OrderSettlement orderSettlement = new OrderSettlement();
            orderSettlement.setPayNo(payNo);
            orderSettlement.setPayType(payParam.getPayType());
            orderSettlement.setUserId(userId);
            orderSettlement.setOrderNumber(orderNumber);
            orderSettlementMapper.updateByOrderNumberAndUserId(orderSettlement);

            Order order = orderMapper.getOrderByOrderNumber(orderNumber);
            prodName.append(order.getProdName()).append(StrUtil.COMMA);
        }
        // 除了ordernumber不一样，其他都一样
        List<OrderSettlement> settlements = orderSettlementMapper.getSettlementsByPayNo(payNo);
        // 应支付的总金额
        double payAmount = 0.0;
        for (OrderSettlement orderSettlement : settlements) {
            payAmount = Arithmetic.add(payAmount, orderSettlement.getPayAmount());
        }

        prodName.substring(0, Math.min(100, prodName.length() - 1));

        PayInfoDto payInfoDto = new PayInfoDto();
        payInfoDto.setBody(prodName.toString());
        payInfoDto.setPayAmount(payAmount);
        payInfoDto.setPayNo(payNo);
        return payInfoDto;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> paySuccess(String payNo, String bizPayNo) {
        List<OrderSettlement> orderSettlements = orderSettlementMapper.selectList(new LambdaQueryWrapper<OrderSettlement>().eq(OrderSettlement::getPayNo, payNo));

        OrderSettlement settlement = orderSettlements.get(0);

        // 订单已支付
        if (settlement.getPayStatus() == 1) {
            throw new WillShopBindException("订单已支付");
        }
        // 修改订单结算信息
        if (orderSettlementMapper.updateToPay(payNo, settlement.getVersion()) < 1) {
            throw new WillShopBindException("结算信息已更改");
        }


        List<String> orderNumbers = orderSettlements.stream().map(OrderSettlement::getOrderNumber).collect(Collectors.toList());

        // 将订单改为已支付状态
        orderMapper.updateByToPaySuccess(orderNumbers, PayType.WECHATPAY.value());

        List<Order> orders = orderNumbers.stream().map(orderNumber -> orderMapper.getOrderByOrderNumber(orderNumber)).collect(Collectors.toList());
        eventPublisher.publishEvent(new PaySuccessOrderEvent(orders));
        return orderNumbers;
    }

}
