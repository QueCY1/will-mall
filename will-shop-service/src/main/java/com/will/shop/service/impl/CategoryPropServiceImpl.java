package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.CategoryProp;
import com.will.shop.dao.CategoryPropMapper;
import com.will.shop.service.CategoryPropService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class CategoryPropServiceImpl extends ServiceImpl<CategoryPropMapper, CategoryProp> implements CategoryPropService {

    private final CategoryPropMapper categoryPropMapper;

}
