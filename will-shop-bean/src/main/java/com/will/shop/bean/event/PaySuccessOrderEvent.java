package com.will.shop.bean.event;

import com.will.shop.bean.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 订单付款成功的事件
 *
 * @author will
 */
@Data
@AllArgsConstructor
public class PaySuccessOrderEvent {

    private List<Order> orders;
}
