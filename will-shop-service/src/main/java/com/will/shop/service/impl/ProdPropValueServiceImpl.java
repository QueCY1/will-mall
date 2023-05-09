package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.ProdPropValue;
import com.will.shop.dao.ProdPropValueMapper;
import com.will.shop.service.ProdPropValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 基本上用mybatis-plus的功能即可实现
 * @author will
 */
@Service
@RequiredArgsConstructor
public class ProdPropValueServiceImpl extends ServiceImpl<ProdPropValueMapper, ProdPropValue> implements ProdPropValueService {

    private final ProdPropValueMapper prodPropValueMapper;

}
