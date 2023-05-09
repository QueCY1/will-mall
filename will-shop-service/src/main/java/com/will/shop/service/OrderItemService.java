package com.will.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.bean.model.OrderItem;

import java.util.List;

/**
 *
 * @author will
 */
public interface OrderItemService extends IService<OrderItem> {

	/**
	 * 根据订单编号获取订单项
	 * @param orderNumber
	 * @return
	 */
	List<OrderItem> getOrderItemsByOrderNumber(String orderNumber);

}
