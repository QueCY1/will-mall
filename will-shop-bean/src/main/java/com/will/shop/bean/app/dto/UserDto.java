package com.will.shop.bean.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author will
 */
@Data
public class UserDto {

    @Schema(description = "用户状态：0禁用 1正常", required = true)
    private Integer status;
    @Schema(description = "token", required = true)
    private String token;

}
