package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.OrderItem;
import com.will.shop.dao.OrderItemMapper;
import com.will.shop.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author will
 */
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    private final OrderItemMapper orderItemMapper;

	@Override
	@Cacheable(cacheNames = "OrderItems", key = "#orderNumber")
	public List<OrderItem> getOrderItemsByOrderNumber(String orderNumber) {
		return orderItemMapper.listByOrderNumber(orderNumber);
	}


}
