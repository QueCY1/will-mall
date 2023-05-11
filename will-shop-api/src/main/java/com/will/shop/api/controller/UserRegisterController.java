package com.will.shop.api.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.will.shop.bean.model.User;
import com.will.shop.bean.app.param.UserRegisterParam;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.common.bo.UserInfoInTokenBO;
import com.will.shop.security.common.enums.SysTypeEnum;
import com.will.shop.security.common.manager.PasswordManager;
import com.will.shop.security.common.manager.TokenStore;
import com.will.shop.security.common.vo.TokenInfoVO;
import com.will.shop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 用户信息
 *
 * @author will
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户注册相关接口")
@RequiredArgsConstructor
public class UserRegisterController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final TokenStore tokenStore;

    private final PasswordManager passwordManager;

    @PostMapping("/register")
    @Operation(summary = "注册", description = "用户注册或绑定手机号接口")
    public ServerResponseEntity<TokenInfoVO> register(@Valid @RequestBody UserRegisterParam userRegisterParam) {
        if (StrUtil.isBlank(userRegisterParam.getNickName())) {
            //如果没有昵称，先用用户名代替
            userRegisterParam.setNickName(userRegisterParam.getUserName());
        }
        // 正在进行申请注册
        if (userService.count(new LambdaQueryWrapper<User>().eq(User::getNickName, userRegisterParam.getNickName())) > 0) {
            // 该用户名已注册，无法重新注册
            throw new WillShopBindException("该用户名已注册，无法重新注册");
        }
        Date now = new Date();
        User user = new User();
        user.setModifyTime(now);
        user.setUserRegtime(now);
        user.setStatus(1);
        user.setNickName(userRegisterParam.getNickName());
        user.setUserMail(userRegisterParam.getUserMail());
        //解前端AES的密码
        String decryptPassword = passwordManager.decryptPassword(userRegisterParam.getPassWord());
        user.setLoginPassword(passwordEncoder.encode(decryptPassword));
        String userId = IdUtil.simpleUUID();
        user.setUserId(userId);
        userService.save(user);
        // 2. 登录
        UserInfoInTokenBO userInfoInTokenBO = new UserInfoInTokenBO();
        userInfoInTokenBO.setUserId(user.getUserId());
        userInfoInTokenBO.setSysType(SysTypeEnum.ORDINARY.value());
        userInfoInTokenBO.setIsAdmin(0);
        userInfoInTokenBO.setEnabled(true);
        TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(userInfoInTokenBO);
        return ServerResponseEntity.success(tokenInfoVO);
    }


    @PutMapping("/updatePwd")
    @Operation(summary = "修改密码", description = "修改密码")
    public ServerResponseEntity<Void> updatePwd(@Valid @RequestBody UserRegisterParam userPwdUpdateParam) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getNickName, userPwdUpdateParam.getNickName()));
        if (user == null) {
            // 无法获取用户信息
            throw new WillShopBindException("无法获取用户信息");
        }
        String decryptPassword = passwordManager.decryptPassword(userPwdUpdateParam.getPassWord());
        if (StrUtil.isBlank(decryptPassword)) {
            // 新密码不能为空
            throw new WillShopBindException("新密码不能为空");
        }
        String password = passwordEncoder.encode(decryptPassword);
        if (StrUtil.equals(password, user.getLoginPassword())) {
            // 新密码不能与原密码相同
            throw new WillShopBindException("新密码不能与原密码相同");
        }
        user.setModifyTime(new Date());
        user.setLoginPassword(password);
        userService.updateById(user);
        return ServerResponseEntity.success();
    }
}
