package com.will.shop.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.User;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

/**
 * 后台的前端会员管理界面没有修改删除的按钮，疑似bug
 */
@RestController
@RequestMapping("/admin/user")
@Tag(name = "会员控制器")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('admin:user:page')")
    public ServerResponseEntity<IPage<User>> page(User user, PageParam<User> page) {
        IPage<User> userPage = userService.page(page, new LambdaQueryWrapper<User>()
                .like(StrUtil.isNotBlank(user.getNickName()), User::getNickName, user.getNickName())
                .eq(user.getStatus() != null, User::getStatus, user.getStatus()));
        for (User userResult : userPage.getRecords()) {
            userResult.setNickName(userResult.getNickName() == null ? "" : userResult.getNickName());
        }
        return ServerResponseEntity.success(userPage);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{userId}")
    @PreAuthorize("@pm.hasPermission('admin:user:info')")
    public ServerResponseEntity<User> info(@PathVariable("userId") String userId) {
        User user = userService.getById(userId);
        user.setNickName(user.getNickName() == null ? "" : user.getNickName());
        return ServerResponseEntity.success(user);
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('admin:user:update')")
    public ServerResponseEntity<Void> update(@RequestBody User user) {
        user.setModifyTime(new Date());
        user.setNickName(user.getNickName() == null ? "" : user.getNickName());
        userService.updateById(user);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping
    @PreAuthorize("@pm.hasPermission('admin:user:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody String[] userIds) {
        userService.removeByIds(Arrays.asList(userIds));
        return ServerResponseEntity.success();
    }
    
}
