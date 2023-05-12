package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.OrderSettlement;
import com.will.shop.dao.OrderSettlementMapper;
import com.will.shop.service.OrderSettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class OrderSettlementServiceImpl extends ServiceImpl<OrderSettlementMapper, OrderSettlement> implements OrderSettlementService {

    private final OrderSettlementMapper orderSettlementMapper;

    @Override
    public void updateSettlementsByPayNo(String outTradeNo, String transactionId) {
        orderSettlementMapper.updateSettlementsByPayNo(outTradeNo, transactionId);
    }

}
