package com.will.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tz_prod_prop_value")
public class ProdPropValue implements Serializable {
    /**
     * 属性值ID
     */
    @TableId
    private Long valueId;

    /**
     * 属性值名称
     */
    private String propValue;

    /**
     * 属性ID: 一个属性ID对应了多个属性值ID
     */
    private Long propId;
}
