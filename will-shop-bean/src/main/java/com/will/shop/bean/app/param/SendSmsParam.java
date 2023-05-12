package com.will.shop.bean.app.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author will
 */
@Data
@Schema(description = "发送验证码参数")
public class SendSmsParam {

    @Schema(description = "手机号")
    @Pattern(regexp = "1[0-9]{10}", message = "请输入正确的手机号")
    private String mobile;

}
