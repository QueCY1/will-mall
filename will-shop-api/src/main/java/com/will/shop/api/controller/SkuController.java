package com.will.shop.api.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.will.shop.bean.app.dto.SkuDto;
import com.will.shop.bean.model.Sku;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.service.SkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author will
 */
@RestController
@RequestMapping("/sku")
@Tag(name = "sku规格接口")
@RequiredArgsConstructor
public class SkuController {

    private final SkuService skuService;

    @GetMapping("/getSkuList")
    @Operation(summary = "通过prodId获取商品全部规格列表", description = "通过prodId获取商品全部规格列表")
    @Parameter(name = "prodId", description = "商品id")
    public ServerResponseEntity<List<SkuDto>> getSkuListByProdId(Long prodId) {
        List<Sku> skus = skuService.list(new LambdaQueryWrapper<Sku>()
                .eq(Sku::getStatus, 1)
                .eq(Sku::getIsDelete, 0)
                .eq(Sku::getProdId, prodId)
        );
        List<SkuDto> skuDtoList = BeanUtil.copyToList(skus, SkuDto.class);
        return ServerResponseEntity.success(skuDtoList);
    }
}
