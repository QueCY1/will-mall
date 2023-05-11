package com.will.shop.bean.app.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author will
 */
@Data
@Schema(description = "地址参数")
public class AddrParam {

    @Schema(description = "地址ID", required = true)
    private Long addrId;

    @NotNull(message = "收货人不能为空")
    @Schema(description = "收货人", required = true)
    private String receiver;

    @NotNull(message = "地址不能为空")
    @Schema(description = "地址", required = true)
    private String addr;

    @Schema(description = "邮编")
    private String postCode;

    @NotNull(message = "手机不能为空")
    @Schema(description = "手机", required = true)
    private String mobile;

    @NotNull(message = "省ID不能为空")
    @Schema(description = "省ID", required = true)
    private Long provinceId;

    @NotNull(message = "城市ID不能为空")
    @Schema(description = "城市ID", required = true)
    private Long cityId;

    @NotNull(message = "区ID不能为空")
    @Schema(description = "区ID", required = true)
    private Long areaId;

    @NotNull(message = "省不能为空")
    @Schema(description = "省", required = true)
    private String province;

    @NotNull(message = "城市不能为空")
    @Schema(description = "城市", required = true)
    private String city;

    @NotNull(message = "区不能为空")
    @Schema(description = "区", required = true)
    private String area;

}
