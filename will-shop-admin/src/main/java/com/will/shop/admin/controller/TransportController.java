package com.will.shop.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.Transport;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.service.TransportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author will
 */
@RestController
@RequestMapping("/shop/transport")
@Tag(name = "运费模板控制器")
@RequiredArgsConstructor
public class TransportController {

    private final TransportService transportService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('shop:transport:page')")
    public ServerResponseEntity<IPage<Transport>> page(Transport transport, PageParam<Transport> page) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        IPage<Transport> transports = transportService.page(page,
                new LambdaQueryWrapper<Transport>()
                        .eq(Transport::getShopId, shopId)
                        .like(StringUtils.isNotBlank(transport.getTransName()), Transport::getTransName, transport.getTransName()));
        return ServerResponseEntity.success(transports);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("@pm.hasPermission('shop:transport:info')")
    public ServerResponseEntity<Transport> info(@PathVariable("id") Long id) {
        Transport transport = transportService.getTransportAndAllItems(id);
        return ServerResponseEntity.success(transport);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pm.hasPermission('shop:transport:save')")
    public ServerResponseEntity<Void> save(@RequestBody Transport transport) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        transport.setShopId(shopId);
        Date createTime = new Date();
        transport.setCreateTime(createTime);
        transportService.insertTransportAndTransfee(transport);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('shop:transport:update')")
    public ServerResponseEntity<Void> update(@RequestBody Transport transport) {
        transportService.updateTransportAndTransfee(transport);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping
    @PreAuthorize("@pm.hasPermission('shop:transport:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody Long[] ids) {
        transportService.deleteTransportAndTransfeeAndTranscity(ids);
        return ServerResponseEntity.success();
    }


    /**
     * 获取运费模板列表
     */
    @GetMapping("/list")
    public ServerResponseEntity<List<Transport>> list() {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        List<Transport> list = transportService.list(new LambdaQueryWrapper<Transport>().eq(Transport::getShopId, shopId));
        return ServerResponseEntity.success(list);
    }

}
