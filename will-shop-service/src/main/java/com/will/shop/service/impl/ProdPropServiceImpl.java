package com.will.shop.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.enums.ProdPropRule;
import com.will.shop.bean.model.ProdProp;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.util.PageAdapter;
import com.will.shop.dao.CategoryPropMapper;
import com.will.shop.dao.ProdPropMapper;
import com.will.shop.dao.ProdPropValueMapper;
import com.will.shop.service.ProdPropService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class ProdPropServiceImpl extends ServiceImpl<ProdPropMapper, ProdProp> implements ProdPropService {

    private final ProdPropMapper prodPropMapper;

    private final ProdPropValueMapper prodPropValueMapper;

    private final CategoryPropMapper categoryPropMapper;

    @Override
    public IPage<ProdProp> pagePropAndValue(ProdProp prodProp, Page<ProdProp> page) {

        page.setRecords(prodPropMapper.listPropAndValue(new PageAdapter(page), prodProp));
        page.setTotal(prodPropMapper.countPropAndValue(prodProp));
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProdPropAndValues(@Valid ProdProp prodProp) {
        ProdProp dbProdProp = prodPropMapper.getProdPropByPropNameAndShopId(prodProp.getPropName(), prodProp.getShopId(), prodProp.getRule());
        if (dbProdProp != null) {
            throw new WillShopBindException("已有相同名称规格");
        }
        prodPropMapper.insert(prodProp);
        if (CollUtil.isEmpty(prodProp.getProdPropValues())) {
            return;
        }
        prodPropValueMapper.insertPropValues(prodProp.getPropId(), prodProp.getProdPropValues());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProdPropAndValues(ProdProp prodProp) {
        ProdProp dbProdProp = prodPropMapper.getProdPropByPropNameAndShopId(prodProp.getPropName(), prodProp.getShopId(), prodProp.getRule());
        if (dbProdProp != null && !Objects.equals(prodProp.getPropId(), dbProdProp.getPropId())) {
            throw new WillShopBindException("已有相同名称规格");
        }
        prodPropMapper.updateById(prodProp);
        // 先删除原有的属性值，再添加新的属性值
        prodPropValueMapper.deleteByPropId(prodProp.getPropId());
        if (CollUtil.isEmpty(prodProp.getProdPropValues())) {
            return;
        }
        prodPropValueMapper.insertPropValues(prodProp.getPropId(), prodProp.getProdPropValues());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProdPropAndValues(Long propId, Integer propRule, Long shopId) {
        int deleteRows = prodPropMapper.deleteByPropId(propId, propRule, shopId);
        if (deleteRows == 0) {
            return;
        }
        // 删除原有的属性值
        prodPropValueMapper.deleteByPropId(propId);

        // 如果是参数，删除参数与分类关联信息
        if (ProdPropRule.ATTRIBUTE.value().equals(propRule)) {
            categoryPropMapper.deleteByPropId(propId);
        }
    }
}