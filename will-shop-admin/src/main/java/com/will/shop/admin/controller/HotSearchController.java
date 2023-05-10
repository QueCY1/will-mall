package com.will.shop.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.HotSearch;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.service.HotSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author will
 */
@RestController
@RequestMapping("/admin/hotSearch")
@Tag(name = "热搜管理控制器")
@RequiredArgsConstructor
public class HotSearchController {

    private final HotSearchService hotSearchService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('admin:hotSearch:page')")
    public ServerResponseEntity<IPage<HotSearch>> page(HotSearch hotSearch, PageParam<HotSearch> page) {
        IPage<HotSearch> hotSearchs = hotSearchService.page(page, new LambdaQueryWrapper<HotSearch>()
                .eq(HotSearch::getShopId, SecurityUtils.getSysUser().getShopId())
                .like(StrUtil.isNotBlank(hotSearch.getContent()), HotSearch::getContent, hotSearch.getContent())
                .like(StrUtil.isNotBlank(hotSearch.getTitle()), HotSearch::getTitle, hotSearch.getTitle())
                .eq(hotSearch.getStatus() != null, HotSearch::getStatus, hotSearch.getStatus())
                .orderByAsc(HotSearch::getSeq));
        return ServerResponseEntity.success(hotSearchs);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{id}")
    public ServerResponseEntity<HotSearch> info(@PathVariable("id") Long id) {
        HotSearch hotSearch = hotSearchService.getById(id);
        return ServerResponseEntity.success(hotSearch);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pm.hasPermission('admin:hotSearch:save')")
    public ServerResponseEntity<Void> save(@RequestBody @Valid HotSearch hotSearch) {
        hotSearch.setRecDate(new Date());
        hotSearch.setShopId(SecurityUtils.getSysUser().getShopId());
        hotSearchService.save(hotSearch);
        //用户端查询热搜会加载缓存，这里清除缓存
        hotSearchService.removeHotSearchDtoCacheByShopId(SecurityUtils.getSysUser().getShopId());
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('admin:hotSearch:update')")
    public ServerResponseEntity<Void> update(@RequestBody @Valid HotSearch hotSearch) {
        hotSearchService.updateById(hotSearch);
        //清除缓存
        hotSearchService.removeHotSearchDtoCacheByShopId(SecurityUtils.getSysUser().getShopId());
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping
    @PreAuthorize("@pm.hasPermission('admin:hotSearch:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody List<Long> ids) {
        hotSearchService.removeByIds(ids);
        //清除缓存
        hotSearchService.removeHotSearchDtoCacheByShopId(SecurityUtils.getSysUser().getShopId());
        return ServerResponseEntity.success();
    }
}
