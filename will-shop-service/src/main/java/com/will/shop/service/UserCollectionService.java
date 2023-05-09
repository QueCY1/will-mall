/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

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
