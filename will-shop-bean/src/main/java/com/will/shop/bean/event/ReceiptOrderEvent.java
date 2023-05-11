package com.will.shop.bean.event;

import com.will.shop.bean.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 确认收货的事件 (没有对应的监听器处理)
 *
 * @author will
 */
@Data
@AllArgsConstructor
public class ReceiptOrderEvent {

    private Order order;
}
