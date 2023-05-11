package com.will.shop.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.bean.app.dto.UserCollectionDto;
import com.will.shop.bean.model.UserCollection;
import com.will.shop.common.util.PageParam;

/**
 * 用户收藏表
 *
 * @author will
 */
public interface UserCollectionService extends IService<UserCollection> {
    /**
     * 分页获取用户收藏
     */
    IPage<UserCollectionDto> getUserCollectionDtoPageByUserId(PageParam<UserCollectionDto> page, String userId);

}
