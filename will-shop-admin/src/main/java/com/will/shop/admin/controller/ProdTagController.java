package com.will.shop.admin.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.model.ProdTag;
import com.will.shop.common.annotation.SysLog;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.security.admin.util.SecurityUtils;
import com.will.shop.service.ProdTagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 商品分组
 *
 * @author will
 */
@RestController
@RequestMapping("/prod/prodTag")
@Tag(name = "分组管理控制器")
@RequiredArgsConstructor
public class ProdTagController {

    private final ProdTagService prodTagService;

    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param prodTag 商品分组标签
     * @return 分页数据
     */
    @GetMapping("/page")
    public ServerResponseEntity<IPage<ProdTag>> getProdTagPage(PageParam<ProdTag> page, ProdTag prodTag) {
        PageParam<ProdTag> tagPage = prodTagService.page(page, new LambdaQueryWrapper<ProdTag>()
                .eq(prodTag.getStatus() != null, ProdTag::getStatus, prodTag.getStatus())
                .like(StrUtil.isNotBlank(prodTag.getTitle()), ProdTag::getTitle, prodTag.getTitle())
                .orderByDesc(ProdTag::getSeq, ProdTag::getCreateTime));
        return ServerResponseEntity.success(tagPage);
    }


    /**
     * 通过id查询商品分组标签
     *
     * @param id id
     * @return 单个数据
     */
    @GetMapping("/info/{id}")
    public ServerResponseEntity<ProdTag> getById(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(prodTagService.getById(id));
    }

    /**
     * 新增商品分组标签
     *
     * @param prodTag 商品分组标签
     * @return 是否新增成功
     */
    @SysLog("新增商品分组标签")
    @PostMapping
    @PreAuthorize("@pm.hasPermission('prod:prodTag:save')")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid ProdTag prodTag) {
        // 查看是否相同的标签
        List<ProdTag> list = prodTagService.list(new LambdaQueryWrapper<ProdTag>().like(ProdTag::getTitle, prodTag.getTitle()));
        if (CollectionUtil.isNotEmpty(list)) {
            throw new WillShopBindException("标签名称已存在，不能添加相同的标签");
        }
        prodTag.setIsDefault(0);
        prodTag.setProdCount(0L);
        prodTag.setCreateTime(new Date());
        prodTag.setUpdateTime(new Date());
        prodTag.setShopId(SecurityUtils.getSysUser().getShopId());
        //删除缓存
        prodTagService.removeProdTag();
        return ServerResponseEntity.success(prodTagService.save(prodTag));
    }

    /**
     * 修改商品分组标签
     *
     * @param prodTag 商品分组标签
     * @return 是否修改成功
     */
    @SysLog("修改商品分组标签")
    @PutMapping
    @PreAuthorize("@pm.hasPermission('prod:prodTag:update')")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid ProdTag prodTag) {
        prodTag.setUpdateTime(new Date());
        prodTagService.removeProdTag();
        return ServerResponseEntity.success(prodTagService.updateById(prodTag));
    }

    /**
     * 通过id删除商品分组标签
     *
     * @param id id
     * @return 是否删除成功
     */
    @SysLog("删除商品分组标签")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pm.hasPermission('prod:prodTag:delete')")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long id) {
        ProdTag prodTag = prodTagService.getById(id);
        if (prodTag.getIsDefault() != 0) {
            throw new WillShopBindException("默认标签不能删除");
        }
        prodTagService.removeProdTag();
        return ServerResponseEntity.success(prodTagService.removeById(id));
    }

    /**
     * 获取商品分组标签列表
     */
    @GetMapping("/listTagList")
    public ServerResponseEntity<List<ProdTag>> listTagList() {
        return ServerResponseEntity.success(prodTagService.listProdTag());

    }


}
