package com.will.shop.api.controller;

import cn.hutool.core.bean.BeanUtil;
import com.will.shop.bean.app.dto.IndexImgDto;
import com.will.shop.bean.model.IndexImg;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.service.IndexImgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author will
 */
@RestController
@Tag(name = "首页轮播图接口")
@RequiredArgsConstructor
public class IndexImgController {

    private final IndexImgService indexImgService;

    /**
     * 首页轮播图接口
     */
    @GetMapping("/indexImgs")
    @Operation(summary = "首页轮播图", description = "获取首页轮播图列表信息")
    public ServerResponseEntity<List<IndexImgDto>> indexImgs() {
        List<IndexImg> indexImgList = indexImgService.listIndexImg();
        List<IndexImgDto> indexImgDtoList = BeanUtil.copyToList(indexImgList, IndexImgDto.class);
        return ServerResponseEntity.success(indexImgDtoList);
    }
}
