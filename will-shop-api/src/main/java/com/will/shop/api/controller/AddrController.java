package com.will.shop.api.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.will.shop.bean.app.dto.UserAddrDto;
import com.will.shop.bean.app.param.AddrParam;
import com.will.shop.bean.model.UserAddr;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.security.api.util.SecurityUtils;
import com.will.shop.service.UserAddrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author will
 */
@RestController
@RequestMapping("/p/address")
@Tag(name = "地址控制器")
@RequiredArgsConstructor
public class AddrController {

    private final UserAddrService userAddrService;

    /**
     * 选择订单配送地址
     */
    @GetMapping("/list")
    @Operation(summary = "用户地址列表", description = "获取用户的所有地址信息")
    public ServerResponseEntity<List<UserAddrDto>> dvyList() {
        String userId = SecurityUtils.getUser().getUserId();
        List<UserAddr> userAdders = userAddrService.list(new LambdaQueryWrapper<UserAddr>()
                .eq(UserAddr::getUserId, userId)
                .orderByDesc(UserAddr::getCommonAddr)
                .orderByDesc(UserAddr::getUpdateTime));
        return ServerResponseEntity.success(BeanUtil.copyToList(userAdders, UserAddrDto.class));
    }

    @PostMapping("/addAddr")
    @Operation(summary = "新增用户地址", description = "新增用户地址")
    public ServerResponseEntity<String> addAddr(@Valid @RequestBody AddrParam addrParam) {
        String userId = SecurityUtils.getUser().getUserId();

        if (addrParam.getAddrId() != null && addrParam.getAddrId() != 0) {
            return ServerResponseEntity.showFailMsg("该地址已存在");
        }
        long addrCount = userAddrService.count(new LambdaQueryWrapper<UserAddr>().eq(UserAddr::getUserId, userId));
        UserAddr userAddr = BeanUtil.copyProperties(addrParam, UserAddr.class);

        //如果一个地址都没有，自动设置成默认地址
        if (addrCount == 0) {
            userAddr.setCommonAddr(1);
        } else {
            userAddr.setCommonAddr(0);
        }
        userAddr.setUserId(userId);
        userAddr.setStatus(1);
        userAddr.setCreateTime(new Date());
        userAddr.setUpdateTime(new Date());
        userAddrService.save(userAddr);
        if (userAddr.getCommonAddr() == 1) {
            // 清除默认地址缓存
            userAddrService.removeUserAddrByUserId(0L, userId);
        }
        return ServerResponseEntity.success("添加地址成功");
    }

    /**
     * 修改订单配送地址
     */
    @PutMapping("/updateAddr")
    @Operation(summary = "修改订单用户地址", description = "修改用户地址")
    public ServerResponseEntity<String> updateAddr(@Valid @RequestBody AddrParam addrParam) {
        String userId = SecurityUtils.getUser().getUserId();

        UserAddr dbUserAddr = userAddrService.getUserAddrByUserId(addrParam.getAddrId(), userId);
        if (dbUserAddr == null) {
            return ServerResponseEntity.showFailMsg("该地址已被删除");
        }

        UserAddr userAddr = BeanUtil.copyProperties(addrParam, UserAddr.class);
        userAddr.setUserId(userId);
        userAddr.setUpdateTime(new Date());
        userAddrService.updateById(userAddr);
        // 清除当前地址缓存
        userAddrService.removeUserAddrByUserId(addrParam.getAddrId(), userId);
        // 清除默认地址缓存
        userAddrService.removeUserAddrByUserId(0L, userId);
        return ServerResponseEntity.success("修改地址成功");
    }

    /**
     * 删除订单配送地址
     */
    @DeleteMapping("/deleteAddr/{addrId}")
    @Operation(summary = "删除订单用户地址", description = "根据地址id，删除用户地址")
    @Parameter(name = "addrId", description = "地址ID", required = true)
    public ServerResponseEntity<String> deleteDvy(@PathVariable("addrId") Long addrId) {
        String userId = SecurityUtils.getUser().getUserId();
        UserAddr userAddr = userAddrService.getUserAddrByUserId(addrId, userId);
        if (userAddr == null) {
            return ServerResponseEntity.showFailMsg("该地址已被删除");
        }
        if (userAddr.getCommonAddr() == 1) {
            return ServerResponseEntity.showFailMsg("默认地址无法删除");
        }
        userAddrService.removeById(addrId);
        userAddrService.removeUserAddrByUserId(addrId, userId);
        return ServerResponseEntity.success("删除地址成功");
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/defaultAddr/{addrId}")
    @Operation(summary = "设置默认地址", description = "根据地址id，设置默认地址")
    public ServerResponseEntity<String> defaultAddr(@PathVariable("addrId") Long addrId) {
        String userId = SecurityUtils.getUser().getUserId();

        userAddrService.updateDefaultUserAddr(addrId, userId);

        userAddrService.removeUserAddrByUserId(0L, userId);
        userAddrService.removeUserAddrByUserId(addrId, userId);
        return ServerResponseEntity.success("修改地址成功");
    }

    /**
     * 获取地址信息订单配送地址
     */
    @GetMapping("/addrInfo/{addrId}")
    @Operation(summary = "获取地址信息", description = "根据地址id，获取地址信息")
    @Parameter(name = "addrId", description = "地址ID", required = true)
    public ServerResponseEntity<UserAddrDto> addrInfo(@PathVariable("addrId") Long addrId) {
        String userId = SecurityUtils.getUser().getUserId();
        UserAddr userAddr = userAddrService.getUserAddrByUserId(addrId, userId);
        if (userAddr == null) {
            throw new WillShopBindException("该地址已被删除");
        }
        UserAddrDto userAddrDto = BeanUtil.copyProperties(userAddr, UserAddrDto.class);
        return ServerResponseEntity.success(userAddrDto);
    }

}
