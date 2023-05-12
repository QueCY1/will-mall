package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.Transcity;
import com.will.shop.dao.TransCityMapper;
import com.will.shop.service.TransCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author will
 */
@Service
@RequiredArgsConstructor
public class TransCityServiceImpl extends ServiceImpl<TransCityMapper, Transcity> implements TransCityService {

    private final TransCityMapper transCityMapper;

}
