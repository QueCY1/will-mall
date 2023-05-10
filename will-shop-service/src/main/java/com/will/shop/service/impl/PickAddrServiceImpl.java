package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.PickAddr;
import com.will.shop.dao.PickAddrMapper;
import com.will.shop.service.PickAddrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author will
 */
@Service
@RequiredArgsConstructor
public class PickAddrServiceImpl extends ServiceImpl<PickAddrMapper, PickAddr> implements PickAddrService {

    private final PickAddrMapper pickAddrMapper;

}
