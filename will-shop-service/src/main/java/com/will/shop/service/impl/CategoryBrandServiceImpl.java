package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.CategoryBrand;
import com.will.shop.dao.CategoryBrandMapper;
import com.will.shop.service.CategoryBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class CategoryBrandServiceImpl extends ServiceImpl<CategoryBrandMapper, CategoryBrand> implements CategoryBrandService {

    private final CategoryBrandMapper categoryBrandMapper;

}
