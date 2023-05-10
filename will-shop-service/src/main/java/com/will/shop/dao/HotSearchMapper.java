package com.will.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.bean.admin.dto.HotSearchDto;
import com.will.shop.bean.model.HotSearch;

import java.util.List;

/**
 * @author will
 */
public interface HotSearchMapper extends BaseMapper<HotSearch> {
    /**
     * 根据店铺id获取热搜列表
     * @param shopId
     * @return
     */
    List<HotSearchDto> getHotSearchDtoByShopId(Long shopId);
}