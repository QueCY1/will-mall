package com.will.shop.security.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用于登录传递账号密码
 * 这是个顶级验证DTO，因为其他的验证方式可能需要验证码，可能不需要，所以是顶级父类
 * 子类在继承这个类后，可以添加额外的验证信息，比如验证码...，也因此类的访问修饰符应该是protected
 * @author will
 */
@Data
public class AuthenticationDTO {

    @NotBlank(message = "用户名不能为空！")
    @Schema(description = "用户名/邮箱/手机号", required = true)
    protected String userName;

    @NotBlank(message = "密码不能为空！")
    @Schema(description = "一般用作密码", required = true)
    protected String passWord;

}
