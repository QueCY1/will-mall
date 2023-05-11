package com.will.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户收藏表
 *
 * @author will
 */
@Data
@TableName("tz_user_collection")
@EqualsAndHashCode
public class UserCollection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 收藏表
     */
    @TableId
    private Long id;
    /**
     * 商品id
     */
    private Long prodId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 收藏时间
     */
    private Date createTime;

}
