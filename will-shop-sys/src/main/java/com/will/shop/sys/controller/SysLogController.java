package com.will.shop.sys.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.sys.model.SysLog;
import com.will.shop.sys.service.SysLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统日志
 *
 * @author will
 */
@RestController
@Tag(name = "系统日志控制器")
@RequestMapping("/sys/log")
@RequiredArgsConstructor
public class SysLogController {

    private final SysLogService sysLogService;

    /**
     * 列表
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('sys:log:page')")
    public ServerResponseEntity<IPage<SysLog>> page(SysLog sysLog, PageParam<SysLog> page) {
        IPage<SysLog> sysLogs = sysLogService.page(page,
                new LambdaQueryWrapper<SysLog>()
                        .like(StrUtil.isNotBlank(sysLog.getUsername()), SysLog::getUsername, sysLog.getUsername())
                        .like(StrUtil.isNotBlank(sysLog.getOperation()), SysLog::getOperation, sysLog.getOperation()));
        return ServerResponseEntity.success(sysLogs);
    }

}
