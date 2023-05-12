package com.will.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.bean.model.SmsLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author will
 */
public interface SmsLogMapper extends BaseMapper<SmsLog> {
    /**
     * 根据手机号和短信类型失效短信
     *
     * @param mobile
     * @param type
     */
    void invalidSmsByMobileAndType(@Param("mobile") String mobile, @Param("type") Integer type);
}