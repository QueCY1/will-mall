package com.will.shop.security.common.controller;

import cn.hutool.core.util.StrUtil;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.common.manager.TokenStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author will
 */
@RestController
@Tag(name = "注销控制器")
@RequiredArgsConstructor
public class LogoutController {

    private final TokenStore tokenStore;

    @PostMapping("/logOut")
    @Operation(summary = "退出登陆", description = "点击退出登陆，清除token，清除菜单缓存")
    public ServerResponseEntity<Void> logOut(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (StrUtil.isBlank(accessToken)) {
            return ServerResponseEntity.success();
        }
        // 删除该用户在该系统当前的token
        tokenStore.deleteCurrentToken(accessToken);
        return ServerResponseEntity.success();
    }
}
