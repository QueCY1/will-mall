package com.will.shop.security.admin.dto;

import com.will.shop.security.common.dto.AuthenticationDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CaptchaAuthenticationDTO extends AuthenticationDTO {

    @Schema(description = "拼图验证码", required = true)
    private String captchaVerification;

}
