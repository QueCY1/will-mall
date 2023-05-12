package com.will.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author will
 */
@Data
@TableName("tz_category_brand")
public class CategoryBrand implements Serializable {
    @TableId

    private Long id;

    /**
     * 分类id
     */

    private Long categoryId;

    /**
     * 品牌id
     */

    private Long brandId;
}