package com.will.shop.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.enums.ProdPropRule;
import com.will.shop.bean.model.ProdProp;
import com.will.shop.bean.model.ProdPropValue;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.service.ProdPropService;
import com.will.shop.service.ProdPropValueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 商品规格管理，这里未分层，没有根据商品分类多套层级，数据库里是设计了 目录-规格 的关联关系的
 */
@RestController
@RequestMapping("/prod/spec")
@Tag(name = "规格管理控制器")
@RequiredArgsConstructor
public class SpecController {

    private final ProdPropService prodPropService;

    private final ProdPropValueService prodPropValueService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('prod:spec:page')")
    public ServerResponseEntity<IPage<ProdProp>> page(ProdProp prodProp, PageParam<ProdProp> page) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
        IPage<ProdProp> list = prodPropService.pagePropAndValue(prodProp, page);
        return ServerResponseEntity.success(list);
    }


    /**
     * 获取所有的规格
     */
    @GetMapping("/list")
    public ServerResponseEntity<List<ProdProp>> list() {
        List<ProdProp> list = prodPropService.list(new LambdaQueryWrapper<ProdProp>()
                .eq(ProdProp::getRule, ProdPropRule.SPEC.value())
                .eq(ProdProp::getShopId, SecurityUtils.getSysUser().getShopId()));
        return ServerResponseEntity.success(list);
    }

    /**
     * 根据规格id获取规格值
     */
    @GetMapping("/listSpecValue/{specId}")
    public ServerResponseEntity<List<ProdPropValue>> listSpecValue(@PathVariable("specId") Long specId) {
        List<ProdPropValue> list = prodPropValueService.list(new LambdaQueryWrapper<ProdPropValue>()
                .eq(ProdPropValue::getPropId, specId));
        return ServerResponseEntity.success(list);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pm.hasPermission('prod:spec:save')")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ProdProp prodProp) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
        prodPropService.saveProdPropAndValues(prodProp);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('prod:spec:update')")
    public ServerResponseEntity<Void> update(@Valid @RequestBody ProdProp prodProp) {
        ProdProp dbProdProp = prodPropService.getById(prodProp.getPropId());
        if (!Objects.equals(dbProdProp.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            throw new WillShopBindException("没有权限获取该商品规格信息");
        }
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
        prodPropService.updateProdPropAndValues(prodProp);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@pm.hasPermission('prod:spec:delete')")
    public ServerResponseEntity<Void> delete(@PathVariable Long id) {
        prodPropService.deleteProdPropAndValues(id, ProdPropRule.SPEC.value(), SecurityUtils.getSysUser().getShopId());
        return ServerResponseEntity.success();
    }

    /**
     * 根据获取规格值最大的自增id
     */
    @GetMapping("/listSpecMaxValueId")
    public ServerResponseEntity<Long> listSpecMaxValueId() {
        ProdPropValue propValue = prodPropValueService.getOne(new LambdaQueryWrapper<ProdPropValue>()
                .orderByDesc(ProdPropValue::getValueId).last("limit 1"));
        return ServerResponseEntity.success(Objects.isNull(propValue) ? 0L : propValue.getValueId());
    }
}
