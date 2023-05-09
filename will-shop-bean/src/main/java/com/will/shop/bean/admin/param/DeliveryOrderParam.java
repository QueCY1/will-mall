package com.will.shop.bean.admin.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 订单快递参数
 * @author will
 */
@Data
public class DeliveryOrderParam {

	@NotBlank(message="订单号不能为空")
	@Schema(description = "订单号" ,required=true)
	private String orderNumber;

	@NotBlank(message="快递公司id不能为空")
	@Schema(description = "快递公司" ,required=true)
	private Long dvyId;

	@NotBlank(message="物流单号不能为空")
	@Schema(description = "物流单号" ,required=true)
	private String dvyFlowId;

}
