package com.will.shop.bean.app.dto;

import lombok.Data;


/**
 * 用户购物数据
 *
 * @author will
 */
@Data
public class UserShoppingDataDto {

    /**
     * 用户消费笔数
     */
    private Double expenseNumber;

    /**
     * 用户消费金额
     */
    private Double sumOfConsumption;
}
