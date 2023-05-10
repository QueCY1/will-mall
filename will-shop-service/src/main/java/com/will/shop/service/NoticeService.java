package com.will.shop.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.bean.app.dto.NoticeDto;
import com.will.shop.bean.model.Notice;

import java.util.List;

/**
 * 公告管理
 *
 * @author will
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 获取公告列表
     *
     * @return
     */
    List<Notice> listNotice();

    /**
     * 删除公告缓存
     */
    void removeNoticeList();

    /**
     * 分页获取公布的公告
     *
     * @param page
     * @return
     */
    Page<NoticeDto> pageNotice(Page<NoticeDto> page);

    /**
     * 根据公告id获取公告
     *
     * @param noticeId
     * @return
     */
    Notice getNoticeById(Long noticeId);

    /**
     * 根据公告id删除公告
     *
     * @param noticeId
     */
    void removeNoticeById(Long noticeId);
}
