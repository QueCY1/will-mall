package com.will.shop.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.enums.MessageStatus;
import com.will.shop.bean.model.Message;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * @author will
 */
@RestController
@RequestMapping("/admin/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pm.hasPermission('admin:message:page')")
    public ServerResponseEntity<IPage<Message>> page(Message message, PageParam<Message> page) {
        IPage<Message> messages = messageService.page(page, new LambdaQueryWrapper<Message>()
                .like(StrUtil.isNotBlank(message.getUserName()), Message::getUserName, message.getUserName())
                .eq(message.getStatus() != null, Message::getStatus, message.getStatus()));
        return ServerResponseEntity.success(messages);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("@pm.hasPermission('admin:message:info')")
    public ServerResponseEntity<Message> info(@PathVariable("id") Long id) {
        Message message = messageService.getById(id);
        return ServerResponseEntity.success(message);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pm.hasPermission('admin:message:save')")
    public ServerResponseEntity<Void> save(@RequestBody Message message) {
        messageService.save(message);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pm.hasPermission('admin:message:update')")
    public ServerResponseEntity<Void> update(@RequestBody Message message) {
        messageService.updateById(message);
        return ServerResponseEntity.success();
    }

    /**
     * 公开留言
     */
    @PutMapping("/release/{id}")
    @PreAuthorize("@pm.hasPermission('admin:message:release')")
    public ServerResponseEntity<Void> release(@PathVariable("id") Long id) {
        Message message = new Message();
        message.setId(id);
        message.setStatus(MessageStatus.RELEASE.value());
        messageService.updateById(message);
        return ServerResponseEntity.success();
    }

    /**
     * 取消公开留言
     */
    @PutMapping("/cancel/{id}")
    @PreAuthorize("@pm.hasPermission('admin:message:cancel')")
    public ServerResponseEntity<Void> cancel(@PathVariable("id") Long id) {
        Message message = new Message();
        message.setId(id);
        message.setStatus(MessageStatus.CANCEL.value());
        messageService.updateById(message);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("@pm.hasPermission('admin:message:delete')")
    public ServerResponseEntity<Void> delete(@PathVariable Long[] ids) {
        messageService.removeByIds(Arrays.asList(ids));
        return ServerResponseEntity.success();
    }
}
