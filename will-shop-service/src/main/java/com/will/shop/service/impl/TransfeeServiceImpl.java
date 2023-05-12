package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.Transfee;
import com.will.shop.dao.TransfeeMapper;
import com.will.shop.service.TransfeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author will
 */
@Service
@RequiredArgsConstructor
public class TransfeeServiceImpl extends ServiceImpl<TransfeeMapper, Transfee> implements TransfeeService {

    private final TransfeeMapper transfeeMapper;

}
