package com.will.shop.api.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonView;
import com.will.shop.bean.app.dto.NoticeDto;
import com.will.shop.bean.model.Notice;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.common.util.PageParam;
import com.will.shop.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author will
 */
@RestController
@RequestMapping("/shop/notice")
@Tag(name = "公告管理接口")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 置顶公告列表接口
     */
    @GetMapping("/topNoticeList")
    @Operation(summary = "置顶公告列表信息", description = "获取所有置顶公告列表信息")
    @JsonView(NoticeDto.NoContent.class)
    public ServerResponseEntity<List<NoticeDto>> getTopNoticeList() {
        List<Notice> noticeList = noticeService.listNotice();
        List<NoticeDto> noticeDtoList = BeanUtil.copyToList(noticeList, NoticeDto.class);
        return ServerResponseEntity.success(noticeDtoList);
    }

    /**
     * 获取公告详情
     */
    @GetMapping("/info/{id}")
    @Operation(summary = "公告详情", description = "获取公告id公告详情")
    @JsonView(NoticeDto.WithContent.class)
    public ServerResponseEntity<NoticeDto> getNoticeById(@PathVariable("id") Long id) {
        Notice notice = noticeService.getNoticeById(id);
        NoticeDto noticeDto = BeanUtil.copyProperties(notice, NoticeDto.class);
        return ServerResponseEntity.success(noticeDto);
    }

    /**
     * 公告列表
     */
    @GetMapping("/noticeList")
    @Operation(summary = "公告列表信息", description = "获取所有公告列表信息")
    @Parameters({
    })
    public ServerResponseEntity<IPage<NoticeDto>> pageNotice(PageParam<NoticeDto> page) {

        return ServerResponseEntity.success(noticeService.pageNotice(page));
    }
}
