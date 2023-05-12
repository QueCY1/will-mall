package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.TransfeeFree;
import com.will.shop.dao.TransfeeFreeMapper;
import com.will.shop.service.TransfeeFreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author will
 */
@Service
@RequiredArgsConstructor
public class TransfeeFreeServiceImpl extends ServiceImpl<TransfeeFreeMapper, TransfeeFree> implements TransfeeFreeService {

    private final TransfeeFreeMapper transfeeFreeMapper;

}
