package com.will.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.bean.model.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author will
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 根据订单编号获取订单项
     *
     * @param orderNumber 订单编号
     */
    List<OrderItem> listByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 插入订单项
     *
     * @param orderItems 订单项
     */
    void insertBatch(List<OrderItem> orderItems);

}