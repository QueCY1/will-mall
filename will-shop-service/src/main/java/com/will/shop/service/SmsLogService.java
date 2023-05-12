package com.will.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.bean.enums.SmsType;
import com.will.shop.bean.model.SmsLog;

import java.util.Map;

/**
 * @author will
 */
public interface SmsLogService extends IService<SmsLog> {
    /**
     * 发送短信
     *
     * @param smsType 短信类型
     * @param userId  用户id
     * @param mobile  手机号
     * @param params  内容
     */
    void sendSms(SmsType smsType, String userId, String mobile, Map<String, String> params);

}
