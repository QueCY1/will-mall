package com.will.shop.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.PickAddr;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ResponseEnum;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.service.PickAddrService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Objects;


/**
 * 自提点管理
 *
 * @author will
 */
@RestController
@RequestMapping("/shop/pickAddr")
@Tag(name = "自提点管理控制器")
@RequiredArgsConstructor
public class PickAddrController {

    private final PickAddrService pickAddrService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('shop:pickAddr:page')")
    public ServerResponseEntity<IPage<PickAddr>> page(PickAddr pickAddr, PageParam<PickAddr> page) {
        IPage<PickAddr> pickAddrIPage = pickAddrService.page(page, new LambdaQueryWrapper<PickAddr>()
                .like(StrUtil.isNotBlank(pickAddr.getAddrName()), PickAddr::getAddrName, pickAddr.getAddrName())
                .orderByDesc(PickAddr::getAddrId));
        return ServerResponseEntity.success(pickAddrIPage);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("@pm.hasPermission('shop:pickAddr:info')")
    public ServerResponseEntity<PickAddr> info(@PathVariable("id") Long id) {
        PickAddr pickAddr = pickAddrService.getById(id);
        return ServerResponseEntity.success(pickAddr);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pm.hasPermission('shop:pickAddr:save')")
    public ServerResponseEntity<Void> save(@Valid @RequestBody PickAddr pickAddr) {
        pickAddr.setShopId(SecurityUtils.getSysUser().getShopId());
        pickAddrService.save(pickAddr);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('shop:pickAddr:update')")
    public ServerResponseEntity<Void> update(@Valid @RequestBody PickAddr pickAddr) {
        PickAddr dbPickAddr = pickAddrService.getById(pickAddr.getAddrId());

        if (!Objects.equals(dbPickAddr.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            throw new WillShopBindException(ResponseEnum.UNAUTHORIZED);
        }
        pickAddrService.updateById(pickAddr);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping
    @PreAuthorize("@pm.hasPermission('shop:pickAddr:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody Long[] ids) {
        pickAddrService.removeByIds(Arrays.asList(ids));
        return ServerResponseEntity.success();
    }
}
