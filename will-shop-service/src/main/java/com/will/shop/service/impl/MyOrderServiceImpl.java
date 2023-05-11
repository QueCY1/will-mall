package com.will.shop.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.app.dto.MyOrderDto;
import com.will.shop.bean.model.Order;
import com.will.shop.common.util.PageAdapter;
import com.will.shop.dao.OrderMapper;
import com.will.shop.service.MyOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class MyOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements MyOrderService {

    private final OrderMapper orderMapper;

    @Override
    public IPage<MyOrderDto> pageMyOrderByUserIdAndStatus(Page<MyOrderDto> page, String userId, Integer status) {
        page.setRecords(orderMapper.listMyOrderByUserIdAndStatus(new PageAdapter(page), userId, status));
        page.setTotal(orderMapper.countMyOrderByUserIdAndStatus(userId, status));
        return page;
    }

}
