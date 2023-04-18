package com.will.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tz_sku")
public class Sku implements Serializable {
    /**
     * 单品ID
     */
    @TableId
    private Long skuId;

    /**
     * 商品ID,属于什么商品
     */
    private Long prodId;

    /**
     * 销售属性组合字符串,格式是p1:v1;p2:v2,如版本:公开版;颜色:深空灰色;内存:64GB
     */
    private String properties;

    /**
     * 原价
     */
    private Double oriPrice;

    /**
     * 价格
     */
    private Double price;

    /**
     * 库存
     */
    private Integer stocks;

    /**
     * 实际库存
     */
    private Integer actualStocks;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 记录时间
     */
    private Date recTime;

    /**
     * 商家编码
     */
    private String partyCode;

    /**
     * 商品条形码
     */
    private String modelId;

    /**
     * sku图片
     */
    private String pic;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 重量
     */
    private Double weight;

    /**
     * 体积
     */
    private Double volume;

    /**
     * 状态：0禁用 1 启用，SKU 状态。编辑商品时，当禁用该sku时，前端将会将该sku置灰
     */
    private Integer status;

    /**
     * 0 正常 1 已被删除
     */
    private Integer isDelete;

}
