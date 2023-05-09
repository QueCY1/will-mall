/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.will.shop.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.app.dto.UserCollectionDto;
import com.will.shop.bean.model.UserCollection;
import com.will.shop.common.util.PageParam;
import com.will.shop.dao.UserCollectionMapper;
import com.will.shop.service.UserCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户收藏表
 *
 * @author xwc
 * @date 2019-04-19 16:57:20
 */
@Service
@RequiredArgsConstructor
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService {

    private final UserCollectionMapper userCollectionMapper;

    @Override
    public IPage<UserCollectionDto> getUserCollectionDtoPageByUserId(PageParam<UserCollectionDto> page, String userId) {
        return userCollectionMapper.getUserCollectionDtoPageByUserId(page, userId);
    }
}
