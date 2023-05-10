package com.will.shop.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.Product;
import com.will.shop.bean.model.Sku;
import com.will.shop.bean.admin.param.ProductParam;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.Json;
import com.will.shop.common.util.PageParam;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.service.BasketService;
import com.will.shop.service.ProdTagReferenceService;
import com.will.shop.service.ProductService;
import com.will.shop.service.SkuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 商品列表、商品发布controller
 *
 * @author will
 */
@RestController
@RequestMapping("/prod/prod")
@Tag(name = "产品管理控制器")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final SkuService skuService;

    private final ProdTagReferenceService prodTagReferenceService;

    private final BasketService basketService;

    /**
     * 分页获取商品信息
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('prod:prod:page')")
    public ServerResponseEntity<IPage<Product>> page(ProductParam product, PageParam<Product> page) {
        IPage<Product> products = productService.page(page,
                new LambdaQueryWrapper<Product>()
                        .like(StrUtil.isNotBlank(product.getProdName()), Product::getProdName, product.getProdName())
                        .eq(Product::getShopId, SecurityUtils.getSysUser().getShopId())
                        .eq(product.getStatus() != null, Product::getStatus, product.getStatus())
                        .orderByDesc(Product::getPutawayTime));
        return ServerResponseEntity.success(products);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{prodId}")
    @PreAuthorize("@pm.hasPermission('prod:prod:info')")
    public ServerResponseEntity<Product> info(@PathVariable("prodId") Long prodId) {
        Product prod = productService.getProductByProdId(prodId);
        if (!Objects.equals(prod.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            throw new WillShopBindException("没有权限获取该商品规格信息");
        }
        List<Sku> skuList = skuService.listByProdId(prodId);
        prod.setSkuList(skuList);

        //获取分组标签(一个商品可能有多个标签)
        List<Long> listTagId = prodTagReferenceService.listTagIdByProdId(prodId);
        prod.setTagList(listTagId);
        return ServerResponseEntity.success(prod);
    }

    /**
     * 保存(新增)
     */
    @PostMapping
    @PreAuthorize("@pm.hasPermission('prod:prod:save')")
    public ServerResponseEntity<String> save(@Valid @RequestBody ProductParam productParam) {
        checkParam(productParam);

        Product product = BeanUtil.copyProperties(productParam, Product.class);
        product.setDeliveryMode(Json.toJsonString(productParam.getDeliveryModeVo()));
        product.setShopId(SecurityUtils.getSysUser().getShopId());
        product.setUpdateTime(new Date());
        if (product.getStatus() == 1) {
            product.setPutawayTime(new Date());
        }
        product.setCreateTime(new Date());
        productService.saveProduct(product);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('prod:prod:update')")
    public ServerResponseEntity<String> update(@Valid @RequestBody ProductParam productParam) {
        checkParam(productParam);
        Product dbProduct = productService.getProductByProdId(productParam.getProdId());
        if (!Objects.equals(dbProduct.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            return ServerResponseEntity.showFailMsg("无法修改非本店铺商品信息");
        }

        List<Sku> dbSkus = skuService.listByProdId(dbProduct.getProdId());


        Product product = BeanUtil.copyProperties(productParam, Product.class);
        product.setDeliveryMode(Json.toJsonString(productParam.getDeliveryModeVo()));
        product.setUpdateTime(new Date());

        //上架时间，数据库里的状态是下架，前端传来的修改变为上架
        if (dbProduct.getStatus() == 0 || productParam.getStatus() == 1) {
            product.setPutawayTime(new Date());
        }
        dbProduct.setSkuList(dbSkus);
        productService.updateProduct(product, dbProduct);

        List<String> userIds = basketService.listUserIdByProdId(product.getProdId());

        //数据库一致性采用的是更新MySQL数据库并删除redis缓存
        for (String userId : userIds) {
            basketService.removeShopCartItemsCacheByUserId(userId);
        }
        for (Sku sku : dbSkus) {
            skuService.removeSkuCacheBySkuId(sku.getSkuId(), sku.getProdId());
        }
        return ServerResponseEntity.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping
    @PreAuthorize("@pm.hasPermission('prod:prod:delete')")
    public ServerResponseEntity<Void> batchDelete(@RequestBody Long[] prodIds) {
        for (Long prodId : prodIds) {
            delete(prodId);
        }
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    public void delete(Long prodId) {
        Product dbProduct = productService.getProductByProdId(prodId);
        if (!Objects.equals(dbProduct.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            throw new WillShopBindException("无法获取非本店铺商品信息");
        }
        List<Sku> dbSkus = skuService.listByProdId(dbProduct.getProdId());
        // 删除商品
        productService.removeProductByProdId(prodId);

        for (Sku sku : dbSkus) {
            skuService.removeSkuCacheBySkuId(sku.getSkuId(), sku.getProdId());
        }

        List<String> userIds = basketService.listUserIdByProdId(prodId);

        for (String userId : userIds) {
            basketService.removeShopCartItemsCacheByUserId(userId);
        }
    }

    /**
     * 更新商品状态
     */
    @PutMapping("/prodStatus")
    @PreAuthorize("@pm.hasPermission('prod:prod:status')")
    public ServerResponseEntity<Void> shopStatus(@RequestParam Long prodId, @RequestParam Integer prodStatus) {
        Product product = new Product();
        product.setProdId(prodId);
        product.setStatus(prodStatus);
        if (prodStatus == 1) {
            product.setPutawayTime(new Date());
        }
        productService.updateById(product);
        productService.removeProductCacheByProdId(prodId);
        List<String> userIds = basketService.listUserIdByProdId(prodId);

        for (String userId : userIds) {
            basketService.removeShopCartItemsCacheByUserId(userId);
        }
        return ServerResponseEntity.success();
    }

    private void checkParam(ProductParam productParam) {
        if (CollectionUtil.isEmpty(productParam.getTagList())) {
            throw new WillShopBindException("请选择产品分组");
        }

        Product.DeliveryModeVO deliveryMode = productParam.getDeliveryModeVo();
        boolean hasDeliverMode = deliveryMode != null
                && (deliveryMode.getHasShopDelivery() || deliveryMode.getHasUserPickUp());
        if (!hasDeliverMode) {
            throw new WillShopBindException("请选择配送方式");
        }
        List<Sku> skuList = productParam.getSkuList();
        boolean isAllUnUse = true;
        for (Sku sku : skuList) {
            if (sku.getStatus() == 1) {
                isAllUnUse = false;
                break;
            }
        }
        if (isAllUnUse) {
            throw new WillShopBindException("至少要启用一种商品规格");
        }
    }
}
