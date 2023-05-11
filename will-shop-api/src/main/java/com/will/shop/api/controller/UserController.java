package com.will.shop.api.controller;

import cn.hutool.core.bean.BeanUtil;
import com.will.shop.bean.app.dto.UserDto;
import com.will.shop.bean.app.param.UserInfoParam;
import com.will.shop.bean.model.User;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.api.util.SecurityUtils;
import com.will.shop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author will
 */
@RestController
@RequestMapping("/p/user")
@Tag(name = "用户接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 查看用户接口
     */
    @GetMapping("/userInfo")
    @Operation(summary = "查看用户信息", description = "根据用户ID（userId）获取用户信息")
    public ServerResponseEntity<UserDto> userInfo() {
        String userId = SecurityUtils.getUser().getUserId();
        User user = userService.getById(userId);
        UserDto userDto = BeanUtil.copyProperties(user, UserDto.class);
        return ServerResponseEntity.success(userDto);
    }

    @PutMapping("/setUserInfo")
    @Operation(summary = "设置用户信息", description = "设置用户信息")
    public ServerResponseEntity<Void> setUserInfo(@RequestBody UserInfoParam userInfoParam) {
        String userId = SecurityUtils.getUser().getUserId();
        User user = new User();
        user.setUserId(userId);
        user.setPic(userInfoParam.getAvatarUrl());
        user.setNickName(userInfoParam.getNickName());
        userService.updateById(user);
        return ServerResponseEntity.success();
    }
}
