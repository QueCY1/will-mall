package com.will.shop.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.ShopDetail;
import com.will.shop.bean.admin.param.ShopDetailParam;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.service.ShopDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author will
 */
@RestController
@RequestMapping("/shop/shopDetail")
@RequiredArgsConstructor
public class ShopDetailController {

    private final ShopDetailService shopDetailService;

    /**
     * 修改分销开关
     */
    @PutMapping("/isDistribution")
    public ServerResponseEntity<Void> updateIsDistribution(@RequestParam Integer isDistribution) {
        ShopDetail shopDetail = new ShopDetail();
        shopDetail.setShopId(SecurityUtils.getSysUser().getShopId());
        shopDetail.setIsDistribution(isDistribution);
        shopDetailService.updateById(shopDetail);
        // 更新完成后删除缓存
        shopDetailService.removeShopDetailCacheByShopId(shopDetail.getShopId());
        return ServerResponseEntity.success();
    }

    /**
     * 获取信息
     */
    @GetMapping("/info")
    @PreAuthorize("@pm.hasPermission('shop:shopDetail:info')")
    public ServerResponseEntity<ShopDetail> info() {
        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(SecurityUtils.getSysUser().getShopId());
        return ServerResponseEntity.success(shopDetail);
    }


    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('shop:shopDetail:page')")
    public ServerResponseEntity<IPage<ShopDetail>> page(ShopDetail shopDetail, PageParam<ShopDetail> page) {
        IPage<ShopDetail> shopDetails = shopDetailService.page(page,
                new LambdaQueryWrapper<ShopDetail>()
                        .like(StrUtil.isNotBlank(shopDetail.getShopName()), ShopDetail::getShopName, shopDetail.getShopName())
                        .orderByDesc(ShopDetail::getShopId));
        return ServerResponseEntity.success(shopDetails);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{shopId}")
    @PreAuthorize("@pm.hasPermission('shop:shopDetail:info')")
    public ServerResponseEntity<ShopDetail> info(@PathVariable("shopId") Long shopId) {
        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(shopId);
        // 店铺图片
        return ServerResponseEntity.success(shopDetail);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pm.hasPermission('shop:shopDetail:save')")
    public ServerResponseEntity<Void> save(@Valid ShopDetailParam shopDetailParam) {
        ShopDetail shopDetail = BeanUtil.copyProperties(shopDetailParam, ShopDetail.class);
        shopDetail.setCreateTime(new Date());
        shopDetail.setShopStatus(1);
        shopDetailService.save(shopDetail);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('shop:shopDetail:update')")
    public ServerResponseEntity<Void> update(@Valid ShopDetailParam shopDetailParam) {
        ShopDetail daShopDetail = shopDetailService.getShopDetailByShopId(shopDetailParam.getShopId());
        ShopDetail shopDetail = BeanUtil.copyProperties(shopDetailParam, ShopDetail.class);
        shopDetail.setUpdateTime(new Date());
        shopDetailService.updateShopDetail(shopDetail, daShopDetail);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@pm.hasPermission('shop:shopDetail:delete')")
    public ServerResponseEntity<Void> delete(@PathVariable Long id) {
        shopDetailService.deleteShopDetailByShopId(id);
        return ServerResponseEntity.success();
    }

    /**
     * 更新店铺状态
     */
    @PutMapping("/shopStatus")
    @PreAuthorize("@pm.hasPermission('shop:shopDetail:shopStatus')")
    public ServerResponseEntity<Void> shopStatus(@RequestParam Long shopId, @RequestParam Integer shopStatus) {
        ShopDetail shopDetail = new ShopDetail();
        shopDetail.setShopId(shopId);
        shopDetail.setShopStatus(shopStatus);
        shopDetailService.updateById(shopDetail);
        // 更新完成后删除缓存
        shopDetailService.removeShopDetailCacheByShopId(shopDetail.getShopId());
        return ServerResponseEntity.success();
    }


    /**
     * 获取所有的店铺名称
     */
    @GetMapping("/listShopName")
    public ServerResponseEntity<List<ShopDetail>> listShopName() {
        List<ShopDetail> list = shopDetailService.list().stream().map((dbShopDetail) -> {
            ShopDetail shopDetail = new ShopDetail();
            shopDetail.setShopId(dbShopDetail.getShopId());
            shopDetail.setShopName(dbShopDetail.getShopName());
            return shopDetail;
        }).collect(Collectors.toList());
        return ServerResponseEntity.success(list);
    }
}
