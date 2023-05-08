package com.will.shop.sys.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.common.annotation.SysLog;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.sys.model.SysConfig;
import com.will.shop.sys.service.SysConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 系统配置信息
 *
 * @author will
 */
@RestController
@Tag(name = "系统配置控制器")
@RequestMapping("/sys/config")
@RequiredArgsConstructor
public class SysConfigController {

    private final SysConfigService sysConfigService;

    /**
     * 所有配置列表
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('sys:config:page')")
    public ServerResponseEntity<IPage<SysConfig>> page(String paramKey, PageParam<SysConfig> page) {
        IPage<SysConfig> sysConfigs = sysConfigService.page(page, new LambdaQueryWrapper<SysConfig>()
                .like(StrUtil.isNotBlank(paramKey), SysConfig::getParamKey, paramKey));
        return ServerResponseEntity.success(sysConfigs);
    }


    /**
     * 配置信息
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("@pm.hasPermission('sys:config:info')")
    public ServerResponseEntity<SysConfig> info(@PathVariable("id") Long id) {
        SysConfig config = sysConfigService.getById(id);
        return ServerResponseEntity.success(config);
    }

    /**
     * 保存配置
     */
    @SysLog("保存配置")
    @PostMapping
    @PreAuthorize("@pm.hasPermission('sys:config:save')")
    public ServerResponseEntity<Void> save(@RequestBody @Valid SysConfig config) {
        sysConfigService.save(config);
        return ServerResponseEntity.success();
    }

    /**
     * 修改配置
     */
    @SysLog("修改配置")
    @PutMapping
    @PreAuthorize("@pm.hasPermission('sys:config:update')")
    public ServerResponseEntity<Void> update(@RequestBody @Valid SysConfig config) {
        sysConfigService.updateById(config);
        return ServerResponseEntity.success();
    }

    /**
     * 删除配置
     */
    @SysLog("删除配置")
    @DeleteMapping
    @PreAuthorize("@pm.hasPermission('sys:config:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody Long[] configIds) {
        sysConfigService.deleteBatch(configIds);
        return ServerResponseEntity.success();
    }

}
