package com.will.shop.bean.event;

import com.will.shop.bean.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 取消订单的事件
 * @author will
 */
@Data
@AllArgsConstructor
public class CancelOrderEvent {

    private Order order;
}
