package com.will.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tz_prod")
public class Product implements Serializable {

    /**
     * 商品ID
     */
    @TableId
    private Long prodId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 简要描述,卖点等
     */
    private String brief;

    /**
     * 商品主图
     */
    private String pic;

    /**
     * 商品图片
     */
    private String imgs;

    /**
     * 默认是1，表示正常状态, -1表示删除, 0下架
     */
    private Integer status;

    /**
     * 商品分类
     */
    private Long categoryId;

    /**
     * 已经销售数量
     */
    private Integer soldNum;

    /**
     * 录入时间
     */
    private Date createTime;

    /**
     * 库存量
     * 基于 sku 的库存数量累加
     */
    private Integer totalStocks;

    /**
     * 原价
     */
    private Double oriPrice;

    /**
     * 现价
     */
    private Double price;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 详细描述
     */
    private String content;

    /**
     * 上架时间
     */
    private Date putawayTime;

    /**
     * 配送方式json
     */
    private String deliveryMode;

    /**
     * 运费模板id
     */
    private Long deliveryTemplateId;


    @Data
    public static class DeliveryModeVO {

        /**
         * 用户自提
         */
        private Boolean hasUserPickUp;

        /**
         * 店铺配送
         */
        private Boolean hasShopDelivery;

    }
}
