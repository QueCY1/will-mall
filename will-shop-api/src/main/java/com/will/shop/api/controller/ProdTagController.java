package com.will.shop.api.controller;

import cn.hutool.core.bean.BeanUtil;
import com.will.shop.bean.app.dto.ProdTagDto;
import com.will.shop.bean.model.ProdTag;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.service.ProdTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小程序首页的分类排序展示
 * @author will
 */
@RestController
@RequestMapping("/prod/tag")
@Tag(name = "商品分组标签接口")
@RequiredArgsConstructor
public class ProdTagController {

    private final ProdTagService prodTagService;

    /**
     * 商品分组标签列表接口
     */
    @GetMapping("/prodTagList")
    @Operation(summary = "商品分组标签列表", description = "获取所有的商品分组列表")
    public ServerResponseEntity<List<ProdTagDto>> getProdTagList() {
        List<ProdTag> prodTagList = prodTagService.listProdTag();
        List<ProdTagDto> prodTagDtoList = BeanUtil.copyToList(prodTagList, ProdTagDto.class);
        return ServerResponseEntity.success(prodTagDtoList);
    }

}
