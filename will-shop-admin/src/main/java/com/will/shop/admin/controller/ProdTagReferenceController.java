package com.will.shop.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.ProdTagReference;
import com.will.shop.common.annotation.SysLog;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.service.ProdTagReferenceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 分组标签引用，与商品的关联关系(已废弃，使用ProductController完成内部功能)
 *
 * @author will
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/generator/prodTagReference")
@Tag(name = "商品与分组关联关系控制器")
public class ProdTagReferenceController {

    private final ProdTagReferenceService prodTagReferenceService;

    /**
     * 分页查询
     *
     * @param page             分页对象
     * @return 分页数据
     */
    @GetMapping("/page")
    public ServerResponseEntity<IPage<ProdTagReference>> getProdTagReferencePage(PageParam<ProdTagReference> page) {
        return ServerResponseEntity.success(prodTagReferenceService.page(page, new LambdaQueryWrapper<>()));
    }


    /**
     * 通过id查询分组标签引用
     *
     * @param referenceId id
     * @return 单个数据
     */
    @GetMapping("/info/{referenceId}")
    public ServerResponseEntity<ProdTagReference> getById(@PathVariable("referenceId") Long referenceId) {
        return ServerResponseEntity.success(prodTagReferenceService.getById(referenceId));
    }

    /**
     * 新增分组标签引用
     *
     * @param prodTagReference 分组标签引用
     * @return 是否新增成功
     */
    @SysLog("新增分组标签引用")
    @PostMapping
    @PreAuthorize("@pm.hasPermission('generator:prodTagReference:save')")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid ProdTagReference prodTagReference) {
        return ServerResponseEntity.success(prodTagReferenceService.save(prodTagReference));
    }

    /**
     * 修改分组标签引用
     *
     * @param prodTagReference 分组标签引用
     * @return 是否修改成功
     */
    @SysLog("修改分组标签引用")
    @PutMapping
    @PreAuthorize("@pm.hasPermission('generator:prodTagReference:update')")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid ProdTagReference prodTagReference) {
        return ServerResponseEntity.success(prodTagReferenceService.updateById(prodTagReference));
    }

    /**
     * 通过id删除分组标签引用
     *
     * @param referenceId id
     * @return 是否删除成功
     */
    @SysLog("删除分组标签引用")
    @DeleteMapping("/{referenceId}")
    @PreAuthorize("@pm.hasPermission('generator:prodTagReference:delete')")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long referenceId) {
        return ServerResponseEntity.success(prodTagReferenceService.removeById(referenceId));
    }

}
