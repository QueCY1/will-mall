package com.will.shop.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.enums.ProdPropRule;
import com.will.shop.bean.model.ProdProp;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.service.ProdPropService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 参数管理(目测被遗弃，由SpecController完成)
 *
 * @author will
 */
@RestController
@RequestMapping("/admin/attribute")
@Tag(name = "参数管理控制器")
@RequiredArgsConstructor
public class AttributeController {

    private final ProdPropService prodPropService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('admin:attribute:page')")
    public ServerResponseEntity<IPage<ProdProp>> page(ProdProp prodProp, PageParam<ProdProp> page) {
        prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
        prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
        IPage<ProdProp> prodPropPage = prodPropService.pagePropAndValue(prodProp, page);
        return ServerResponseEntity.success(prodPropPage);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("@pm.hasPermission('admin:attribute:info')")
    public ServerResponseEntity<ProdProp> info(@PathVariable("id") Long id) {
        ProdProp prodProp = prodPropService.getById(id);
        return ServerResponseEntity.success(prodProp);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pm.hasPermission('admin:attribute:save')")
    public ServerResponseEntity<Void> save(@Valid ProdProp prodProp) {
        prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
        prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
        prodPropService.saveProdPropAndValues(prodProp);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('admin:attribute:update')")
    public ServerResponseEntity<Void> update(@Valid ProdProp prodProp) {
        ProdProp dbProdProp = prodPropService.getById(prodProp.getPropId());
        if (!Objects.equals(dbProdProp.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            throw new WillShopBindException("没有权限获取该商品规格信息");
        }
        prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
        prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
        prodPropService.updateProdPropAndValues(prodProp);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@pm.hasPermission('admin:attribute:delete')")
    public ServerResponseEntity<Void> delete(@PathVariable Long id) {
        prodPropService.deleteProdPropAndValues(id, ProdPropRule.ATTRIBUTE.value(), SecurityUtils.getSysUser().getShopId());
        return ServerResponseEntity.success();
    }
}
