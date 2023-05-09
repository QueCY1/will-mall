package com.will.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.will.shop.bean.app.dto.UserCollectionDto;
import com.will.shop.bean.model.UserCollection;
import com.will.shop.common.util.PageParam;

/**
 * 用户收藏表
 *
 * @author will
 */
public interface UserCollectionMapper extends BaseMapper<UserCollection> {
    /**
     * 分页获取用户收藏
     */
    IPage<UserCollectionDto> getUserCollectionDtoPageByUserId(PageParam<UserCollectionDto> page, String userId);

}
