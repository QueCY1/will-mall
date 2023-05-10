package com.will.shop.bean.app.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author will
 */
@Schema(description = "购物车参数")
@Data
public class ShopCartParam {

    @Schema(description = "购物项id")
    private Long basketId;

    @Schema(description = "活动id,传0则不参与该活动")
    private Long discountId;
}
