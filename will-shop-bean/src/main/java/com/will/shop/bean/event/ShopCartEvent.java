package com.will.shop.bean.event;

import com.will.shop.bean.app.dto.ShopCartDto;
import com.will.shop.bean.app.dto.ShopCartItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 购物车商品发生改变时的事件
 * @author will
 */
@Data
@AllArgsConstructor
public class ShopCartEvent {

    /**
     * 将要组装的单个店铺的店铺信息
     */
    private ShopCartDto shopCartDto;

    /**
     * 该店铺下的所有商品信息
     */
    private List<ShopCartItemDto> shopCartItemDtoList;
}
