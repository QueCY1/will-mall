package com.will.shop.security.common.controller;

import cn.hutool.core.bean.BeanUtil;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.common.bo.TokenInfoBO;
import com.will.shop.security.common.dto.RefreshTokenDTO;
import com.will.shop.security.common.manager.TokenStore;
import com.will.shop.security.common.vo.TokenInfoVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author will
 */
@RestController
@Tag(name = "token控制器")
@RequiredArgsConstructor
public class TokenController {

    private final TokenStore tokenStore;


    @PostMapping("/token/refresh")
    public ServerResponseEntity<TokenInfoVO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        TokenInfoBO tokenInfoServerResponseEntity = tokenStore.refreshToken(refreshTokenDTO.getRefreshToken());
        return ServerResponseEntity.success(BeanUtil.copyProperties(tokenInfoServerResponseEntity, TokenInfoVO.class));
    }

}
