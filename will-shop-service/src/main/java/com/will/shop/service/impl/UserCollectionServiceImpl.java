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
 * @author will
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
