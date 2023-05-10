package com.will.shop.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.Brand;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


/**
 * 品牌管理
 *
 * @author will
 */
@RestController
@RequestMapping("/admin/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('admin:brand:page')")
    public ServerResponseEntity<IPage<Brand>> page(Brand brand, PageParam<Brand> page) {
        IPage<Brand> brands = brandService.page(page, new LambdaQueryWrapper<Brand>()
                .like(StrUtil.isNotBlank(brand.getBrandName()), Brand::getBrandName, brand.getBrandName())
                .orderByAsc(Brand::getFirstChar));
        return ServerResponseEntity.success(brands);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("@pm.hasPermission('admin:brand:info')")
    public ServerResponseEntity<Brand> info(@PathVariable("id") Long id) {
        Brand brand = brandService.getById(id);
        return ServerResponseEntity.success(brand);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pm.hasPermission('admin:brand:save')")
    public ServerResponseEntity<Void> save(@Valid Brand brand) {
        Brand dbBrand = brandService.getByBrandName(brand.getBrandName());
        if (dbBrand != null) {
            throw new WillShopBindException("该品牌名称已存在");
        }
        brandService.save(brand);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('admin:brand:update')")
    public ServerResponseEntity<Void> update(@Valid Brand brand) {
        Brand dbBrand = brandService.getByBrandName(brand.getBrandName());
        if (dbBrand != null && !Objects.equals(dbBrand.getBrandId(), brand.getBrandId())) {
            throw new WillShopBindException("该品牌名称已存在");
        }
        brandService.updateById(brand);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@pm.hasPermission('admin:brand:delete')")
    public ServerResponseEntity<Void> delete(@PathVariable Long id) {
        brandService.deleteByBrand(id);
        return ServerResponseEntity.success();
    }

}
