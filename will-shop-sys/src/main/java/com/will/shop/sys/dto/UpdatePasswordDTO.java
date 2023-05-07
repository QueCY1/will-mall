package com.will.shop.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author will
 */
@Data
@Schema(description = "更新密码参数")
public class UpdatePasswordDTO {

    @NotBlank(message = "旧密码不能为空")
    @Size(max = 50)
    @Schema(description = "旧密码", required = true)
    private String password;

    @NotBlank(message = "新密码不能为空")
    @Size(max = 50)
    @Schema(description = "新密码", required = true)
    private String newPassword;

}
