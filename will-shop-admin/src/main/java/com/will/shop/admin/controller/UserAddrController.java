package com.will.shop.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.UserAddr;
import com.will.shop.common.annotation.SysLog;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.service.UserAddrService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户地址管理
 *
 * @author will
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "用户地址管理控制器")
@RequestMapping("/user/addr")
public class UserAddrController {

    private final UserAddrService userAddrService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public ServerResponseEntity<IPage<UserAddr>> getUserAddrPage(PageParam<UserAddr> page) {
        return ServerResponseEntity.success(userAddrService.page(page, new LambdaQueryWrapper<>()));
    }


    /**
     * 通过id查询用户地址管理
     *
     * @param addrId id
     * @return 单个数据
     */
    @GetMapping("/info/{addrId}")
    public ServerResponseEntity<UserAddr> getById(@PathVariable("addrId") Long addrId) {
        return ServerResponseEntity.success(userAddrService.getById(addrId));
    }

    /**
     * 新增用户地址管理
     *
     * @param userAddr 用户地址管理
     * @return 是否新增成功
     */
    @SysLog("新增用户地址管理")
    @PostMapping
    @PreAuthorize("@pm.hasPermission('user:addr:save')")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid UserAddr userAddr) {
        return ServerResponseEntity.success(userAddrService.save(userAddr));
    }

    /**
     * 修改用户地址管理
     *
     * @param userAddr 用户地址管理
     * @return 是否修改成功
     */
    @SysLog("修改用户地址管理")
    @PutMapping
    @PreAuthorize("@pm.hasPermission('user:addr:update')")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid UserAddr userAddr) {
        return ServerResponseEntity.success(userAddrService.updateById(userAddr));
    }

    /**
     * 通过id删除用户地址管理
     *
     * @param addrId id
     * @return 是否删除成功
     */
    @SysLog("删除用户地址管理")
    @DeleteMapping("/{addrId}")
    @PreAuthorize("@pm.hasPermission('user:addr:delete')")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long addrId) {
        return ServerResponseEntity.success(userAddrService.removeById(addrId));
    }

}
