package com.will.shop.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.will.shop.bean.app.dto.NoticeDto;
import com.will.shop.bean.model.Notice;

/**
 * 公告管理
 *
 * @author will
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    /**
     * 分页获取公布的公告
     *
     * @param page
     * @return
     */
    Page<NoticeDto> pageNotice(Page<NoticeDto> page);
}
