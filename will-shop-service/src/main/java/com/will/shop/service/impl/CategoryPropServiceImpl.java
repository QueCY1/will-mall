/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.model.CategoryProp;
import com.yami.shop.dao.CategoryPropMapper;
import com.yami.shop.service.CategoryPropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lanhai
 */
@Service
public class CategoryPropServiceImpl extends ServiceImpl<CategoryPropMapper, CategoryProp> implements CategoryPropService {

    @Autowired
    private CategoryPropMapper categoryPropMapper;

}