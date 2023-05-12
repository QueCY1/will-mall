package com.will.shop.service;

import com.will.shop.bean.app.dto.ProductItemDto;
import com.will.shop.bean.model.UserAddr;

/**
 * @author will
 */
public interface TransportManagerService {
    /**
     * 计算运费
     *
     * @param productItem
     * @param userAddr
     * @return
     */
    Double calculateTransfee(ProductItemDto productItem, UserAddr userAddr);
}
