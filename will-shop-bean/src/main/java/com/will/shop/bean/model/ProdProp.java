package com.will.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tz_prod_prop")
public class ProdProp implements Serializable {
    /**
     * 属性id
     */
    @TableId
    private Long propId;

    /**
     * 属性名称
     */
    private String propName;

    private Long shopId;

}
