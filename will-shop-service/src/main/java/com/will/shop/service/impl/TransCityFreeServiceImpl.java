package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.TranscityFree;
import com.will.shop.dao.TransCityFreeMapper;
import com.will.shop.service.TransCityFreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lgh on 2018/12/20.
 */
@Service
public class TransCityFreeServiceImpl extends ServiceImpl<TransCityFreeMapper, TranscityFree> implements TransCityFreeService {

    @Autowired
    private TransCityFreeMapper transcityFreeMapper;

}
