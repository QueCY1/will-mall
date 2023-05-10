package com.will.shop.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.ProdComm;
import com.will.shop.common.annotation.SysLog;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.service.ProdCommService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 商品评论(目前并未完善)
 *
 * @author will
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/prod/prodComm")
@Tag(name = "商品评论控制器")
public class ProdCommController {

    private final ProdCommService prodCommService;

    /**
     * 分页查询
     *
     * @param page     分页对象
     * @param prodComm 商品评论
     * @return 分页数据
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('prod:prodComm:page')")
    public ServerResponseEntity<IPage<ProdComm>> getProdCommPage(PageParam<ProdComm> page, ProdComm prodComm) {
        return ServerResponseEntity.success(prodCommService.getProdCommPage(page, prodComm));
    }


    /**
     * 通过id查询商品评论
     *
     * @param prodCommId id
     * @return 单个数据
     */
    @GetMapping("/info/{prodCommId}")
    @PreAuthorize("@pm.hasPermission('prod:prodComm:info')")
    public ServerResponseEntity<ProdComm> getById(@PathVariable("prodCommId") Long prodCommId) {
        return ServerResponseEntity.success(prodCommService.getById(prodCommId));
    }

    /**
     * 新增商品评论
     *
     * @param prodComm 商品评论
     * @return 是否新增成功
     */
    @SysLog("新增商品评论")
    @PostMapping
    @PreAuthorize("@pm.hasPermission('prod:prodComm:save')")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid ProdComm prodComm) {
        return ServerResponseEntity.success(prodCommService.save(prodComm));
    }

    /**
     * 修改商品评论
     *
     * @param prodComm 商品评论
     * @return 是否修改成功
     */
    @SysLog("修改商品评论")
    @PutMapping
    @PreAuthorize("@pm.hasPermission('prod:prodComm:update')")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid ProdComm prodComm) {
        prodComm.setReplyTime(new Date());
        return ServerResponseEntity.success(prodCommService.updateById(prodComm));
    }

    /**
     * 通过id删除商品评论
     *
     * @param prodCommId id
     * @return 是否删除成功
     */
    @SysLog("删除商品评论")
    @DeleteMapping("/{prodCommId}")
    @PreAuthorize("@pm.hasPermission('prod:prodComm:delete')")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long prodCommId) {
        return ServerResponseEntity.success(prodCommService.removeById(prodCommId));
    }

}
