package com.will.shop.api.controller;

import com.will.shop.bean.model.Area;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.service.AreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author will
 */
@RestController
@RequestMapping("/p/area")
@Tag(name = "省市区控制器")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    /**
     * 分页获取
     */
    @GetMapping("/listByPid")
    @Operation(summary = "获取省市区信息", description = "根据省市区的pid获取地址信息")
    @Parameter(name = "pid", description = "省市区的pid(pid为0获取所有省份)", required = true)
    public ServerResponseEntity<List<Area>> listByPid(Long pid) {
        List<Area> list = areaService.listByPid(pid);
        return ServerResponseEntity.success(list);
    }

}
