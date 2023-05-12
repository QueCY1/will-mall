package com.will.shop.service;

import com.will.shop.bean.app.param.PayParam;
import com.will.shop.bean.app.pay.PayInfoDto;

import java.util.List;

/**
 * @author will
 */
public interface PayService {

    /**
     * 支付
     *
     * @param userId
     * @param payParam
     * @return
     */
    PayInfoDto pay(String userId, PayParam payParam);

    /**
     * 支付成功
     *
     * @param payNo
     * @param bizPayNo
     * @return
     */
    List<String> paySuccess(String payNo, String bizPayNo);

}
