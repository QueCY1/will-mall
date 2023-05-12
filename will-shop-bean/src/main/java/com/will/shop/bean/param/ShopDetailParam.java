package com.will.shop.bean.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author will
 */
@Data
public class ShopDetailParam {

    private Long shopId;

    /**
     * 店铺名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一
     */
    @NotBlank(message = "店铺名称不能为空")
    @Size(max = 50, message = "商品名称长度应该小于{max}")
    private String shopName;

    /**
     * 店铺简介(可修改)
     */
    @Size(max = 200, message = "店铺简介长度应该小于{max}")
    private String intro;

    /**
     * 店铺公告(可修改)
     */
    @Size(max = 50, message = "店铺公告长度应该小于{max}")
    private String shopNotice;

    /**
     * 店铺联系电话
     */
    @NotBlank(message = "店铺联系电话不能为空")
    @Size(max = 20, message = "店铺公告长度应该小于{max}")
    private String tel;

    /**
     * 店铺详细地址
     */
    @NotBlank(message = "店铺详细地址不能为空")
    @Size(max = 100, message = "店铺公告长度应该小于{max}")
    private String shopAddress;

    /**
     * 店铺所在省份（描述）
     */
    @NotBlank(message = "店铺所在省份不能为空")
    @Size(max = 10, message = "店铺所在省份长度应该小于{max}")
    private String province;

    /**
     * 店铺所在城市（描述）
     */
    @NotBlank(message = "店铺所在城市不能为空")
    @Size(max = 10, message = "店铺所在城市长度应该小于{max}")
    private String city;

    /**
     * 店铺所在区域（描述）
     */
    @NotBlank(message = "店铺所在区域不能为空")
    @Size(max = 10, message = "店铺所在省份区域长度应该小于{max}")
    private String area;

    /**
     * 店铺省市区代码，用于回显
     */
    @NotBlank(message = "店铺省市区代码不能为空")
    @Size(max = 20, message = "店铺省市区代码长度应该小于{max}")
    private String pcaCode;

    /**
     * 店铺logo(可修改)
     */
    @NotBlank(message = "店铺logo不能为空")
    @Size(max = 200, message = "店铺logo长度应该小于{max}")
    private String shopLogo;

    /**
     * 店铺相册
     */
    @Size(max = 1000, message = "店铺相册长度应该小于{max}")
    private String shopPhotos;

    /**
     * 每天营业时间段(可修改)
     */
    @NotBlank(message = "每天营业时间段不能为空")
    @Size(max = 100, message = "每天营业时间段长度应该小于{max}")
    private String openTime;

    /**
     * 店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改
     */
    private Integer shopStatus;

}
