package com.will.shop.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.IndexImg;
import com.will.shop.bean.model.Product;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.service.IndexImgService;
import com.will.shop.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

/**
 * @author will
 */
@RestController
@RequestMapping("/admin/indexImg")
@Tag(name = "轮播图管理控制器")
@RequiredArgsConstructor
public class IndexImgController {

    private final IndexImgService indexImgService;

    private final ProductService productService;


    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('admin:indexImg:page')")
    public ServerResponseEntity<IPage<IndexImg>> page(IndexImg indexImg, PageParam<IndexImg> page) {
        IPage<IndexImg> indexImgPage = indexImgService.page(page,
                new LambdaQueryWrapper<IndexImg>()
                        .eq(indexImg.getStatus() != null, IndexImg::getStatus, indexImg.getStatus())
                        .orderByAsc(IndexImg::getSeq));
        return ServerResponseEntity.success(indexImgPage);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{imgId}")
    @PreAuthorize("@pm.hasPermission('admin:indexImg:info')")
    public ServerResponseEntity<IndexImg> info(@PathVariable("imgId") Long imgId) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        IndexImg indexImg = indexImgService.getOne(new LambdaQueryWrapper<IndexImg>()
                .eq(IndexImg::getShopId, shopId)
                .eq(IndexImg::getImgId, imgId));
        if (Objects.nonNull(indexImg.getRelation())) {
            Product product = productService.getProductByProdId(indexImg.getRelation());
            indexImg.setPic(product.getPic());
            indexImg.setProdName(product.getProdName());
        }
        return ServerResponseEntity.success(indexImg);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pm.hasPermission('admin:indexImg:save')")
    public ServerResponseEntity<Void> save(@RequestBody @Valid IndexImg indexImg) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        indexImg.setShopId(shopId);
        indexImg.setUploadTime(new Date());
        checkProdStatus(indexImg);
        indexImgService.save(indexImg);
        indexImgService.removeIndexImgCache();
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('admin:indexImg:update')")
    public ServerResponseEntity<Void> update(@RequestBody @Valid IndexImg indexImg) {
        checkProdStatus(indexImg);
        indexImgService.saveOrUpdate(indexImg);
        indexImgService.removeIndexImgCache();
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping
    @PreAuthorize("@pm.hasPermission('admin:indexImg:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody Long[] ids) {
        indexImgService.deleteIndexImgByIds(ids);
        indexImgService.removeIndexImgCache();
        return ServerResponseEntity.success();
    }

    private void checkProdStatus(IndexImg indexImg) {
        if (!Objects.equals(indexImg.getType(), 0)) {
            return;
        }
        if (Objects.isNull(indexImg.getRelation())) {
            throw new WillShopBindException("请选择商品");
        }
        Product product = productService.getById(indexImg.getRelation());
        if (Objects.isNull(product)) {
            throw new WillShopBindException("商品信息不存在");
        }
        if (!Objects.equals(product.getStatus(), 1)) {
            throw new WillShopBindException("该商品未上架，请选择别的商品");
        }
    }
}
