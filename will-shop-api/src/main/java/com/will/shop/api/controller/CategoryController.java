package com.will.shop.api.controller;

import cn.hutool.core.bean.BeanUtil;
import com.will.shop.bean.app.dto.CategoryDto;
import com.will.shop.bean.model.Category;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author will
 */
@RestController
@RequestMapping("/category")
@Tag(name = "分类接口控制器")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 分类信息列表接口
     */
    @GetMapping("/categoryInfo")
    @Operation(summary = "分类信息列表", description = "获取所有的产品分类信息，顶级分类的parentId为0,默认为顶级分类")
    @Parameter(name = "parentId", description = "分类ID", required = false)
    public ServerResponseEntity<List<CategoryDto>> categoryInfo(@RequestParam(value = "parentId", defaultValue = "0") Long parentId) {
        List<Category> categories = categoryService.listByParentId(parentId);
        List<CategoryDto> categoryDtoList = BeanUtil.copyToList(categories, CategoryDto.class);
        return ServerResponseEntity.success(categoryDtoList);
    }

}
