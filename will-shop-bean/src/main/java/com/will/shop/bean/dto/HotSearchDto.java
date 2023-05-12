package com.will.shop.bean.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author will
 */
@Schema(description = "热搜数据")
@Data
public class HotSearchDto implements Serializable {

    @Schema(description = "热搜id")
    private Long hotSearchId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

}