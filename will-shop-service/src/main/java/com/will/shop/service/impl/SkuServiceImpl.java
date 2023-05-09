package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.Sku;
import com.will.shop.dao.SkuMapper;
import com.will.shop.service.SkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    private final SkuMapper skuMapper;

    @Override
    @Cacheable(cacheNames = "skuList", key = "#prodId")
    public List<Sku> listByProdId(Long prodId) {
        return skuMapper.listByProdId(prodId);
    }

    @Override
    @Cacheable(cacheNames = "sku", key = "#skuId")
    public Sku getSkuBySkuId(Long skuId) {
        return skuMapper.selectById(skuId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "sku", key = "#skuId"),
            @CacheEvict(cacheNames = "skuList", key = "#prodId")
    })
    public void removeSkuCacheBySkuId(Long skuId, Long prodId) {

    }

}
