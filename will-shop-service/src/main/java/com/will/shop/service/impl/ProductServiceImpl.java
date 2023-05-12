package com.will.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.app.dto.ProductDto;
import com.will.shop.bean.app.dto.TagProductDto;
import com.will.shop.bean.dto.SearchProdDto;
import com.will.shop.bean.model.ProdTagReference;
import com.will.shop.bean.model.Product;
import com.will.shop.bean.model.Sku;
import com.will.shop.common.util.Arithmetic;
import com.will.shop.common.util.PageParam;
import com.will.shop.dao.ProdTagReferenceMapper;
import com.will.shop.dao.ProductMapper;
import com.will.shop.dao.SkuMapper;
import com.will.shop.service.AttachFileService;
import com.will.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;

    private final SkuMapper skuMapper;

    private final AttachFileService attachFileService;

    private final ProdTagReferenceMapper prodTagReferenceMapper;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProduct(Product product) {
        productMapper.insert(product);
        if (CollectionUtil.isNotEmpty(product.getSkuList())) {
            skuMapper.insertBatch(product.getProdId(), product.getSkuList());
        }
        prodTagReferenceMapper.insertBatch(product.getShopId(), product.getProdId(), product.getTagList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = "product", key = "#prodId"),
            @CacheEvict(cacheNames = "skuList", key = "#prodId")
    })
    public void updateProduct(Product product, Product dbProduct) {

        productMapper.updateById(product);
        List<Long> dbSkuIds = dbProduct.getSkuList().stream().map(Sku::getSkuId).toList();
        // 将所有该商品的sku标记为已删除状态
        skuMapper.deleteByProdId(product.getProdId());

        // 接口传入sku列表
        List<Sku> skuList = product.getSkuList();

        if (CollectionUtil.isEmpty(skuList)) {
            return;
        }

        List<Sku> insertSkuList = new ArrayList<>();
        for (Sku sku : skuList) {
            sku.setIsDelete(0);
            // 如果数据库中原有sku就更新，否者就插入
            if (dbSkuIds.contains(sku.getSkuId())) {
                skuMapper.updateById(sku);
            } else {
                insertSkuList.add(sku);
            }
        }
        // 批量插入sku
        if (CollectionUtil.isNotEmpty(insertSkuList)) {
            skuMapper.insertBatch(product.getProdId(), insertSkuList);
        }

        //更新分组信息
        List<Long> tagList = product.getTagList();
        if (CollectionUtil.isNotEmpty(tagList)) {
            prodTagReferenceMapper.delete(new LambdaQueryWrapper<ProdTagReference>().eq(ProdTagReference::getProdId, product.getProdId()));
            prodTagReferenceMapper.insertBatch(dbProduct.getShopId(), product.getProdId(), tagList);
        }
    }


    @Override
    @Cacheable(cacheNames = "product", key = "#prodId")
    public Product getProductByProdId(Long prodId) {
        return productMapper.selectById(prodId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = "product", key = "#prodId"),
            @CacheEvict(cacheNames = "skuList", key = "#prodId")
    })
    public void removeProductByProdId(Long prodId) {
        Product dbProduct = getProductByProdId(prodId);

        productMapper.deleteById(prodId);

        skuMapper.deleteByProdId(prodId);

        //删除商品关联的分组标签
        prodTagReferenceMapper.delete(new LambdaQueryWrapper<ProdTagReference>()
                .eq(ProdTagReference::getProdId, prodId));

        // 删除数据库中的商品图片
//        if (StrUtil.isNotBlank(dbProduct.getImgs())) {
//            String[] imgs = dbProduct.getImgs().split(StrUtil.COMMA);
//            for (String img : imgs) {
//                attachFileService.deleteFile(img);
//            }
//        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "product", key = "#prodId"),
            @CacheEvict(cacheNames = "skuList", key = "#prodId")
    })
    public void removeProductCacheByProdId(Long prodId) {
    }

    @Override
    public IPage<ProductDto> pageByPutAwayTime(IPage<ProductDto> page) {
        return productMapper.pageByPutAwayTime(page);
    }

    @Override
    public IPage<ProductDto> pageByTagId(Page<ProductDto> page, Long tagId) {
        return productMapper.pageByTagId(page, tagId);
    }

    @Override
    public IPage<ProductDto> moreBuyProdList(Page<ProductDto> page) {
        return productMapper.moreBuyProdList(page);
    }

    @Override
    public IPage<ProductDto> pageByCategoryId(Page<ProductDto> page, Long categoryId) {
        return productMapper.pageByCategoryId(page, categoryId);
    }

    @Override
    public IPage<SearchProdDto> getSearchProdDtoPageByProdName(Page<SearchProdDto> page, String prodName, Integer sort, Integer orderBy) {
        IPage<SearchProdDto> searchProdDtoPage = productMapper.getSearchProdDtoPageByProdName(page, prodName, sort, orderBy);
        for (SearchProdDto searchProdDto : searchProdDtoPage.getRecords()) {
            //计算出好评率
            if (searchProdDto.getPraiseNumber() == 0 || searchProdDto.getProdCommNumber() == 0) {
                searchProdDto.setPositiveRating(0.0);
            } else {
                searchProdDto.setPositiveRating(Arithmetic.mul(
                        Arithmetic.div(searchProdDto.getPraiseNumber(), searchProdDto.getProdCommNumber()), 100));
            }
        }
        return searchProdDtoPage;
    }

    @Override
    public List<TagProductDto> tagProdList() {
        return productMapper.tagProdList();
    }

    @Override
    public IPage<ProductDto> collectionProds(PageParam<ProductDto> page, String userId) {
        return productMapper.collectionProds(page, userId);
    }


}
