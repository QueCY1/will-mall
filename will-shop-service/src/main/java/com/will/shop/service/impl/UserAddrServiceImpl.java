package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.UserAddr;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.dao.UserAddrMapper;
import com.will.shop.service.UserAddrService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class UserAddrServiceImpl extends ServiceImpl<UserAddrMapper, UserAddr> implements UserAddrService {

    private final UserAddrMapper userAddrMapper;

    @Override
    public UserAddr getDefaultUserAddr(String userId) {
        return userAddrMapper.getDefaultUserAddr(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDefaultUserAddr(Long addrId, String userId) {
        userAddrMapper.removeDefaultUserAddr(userId);
        int setCount = userAddrMapper.setDefaultUserAddr(addrId, userId);
        if (setCount == 0) {
            throw new WillShopBindException("无法修改用户默认地址，请稍后再试");
        }
    }

    @Override
    @CacheEvict(cacheNames = "UserAddrDto", key = "#userId+':'+#addrId")
    public void removeUserAddrByUserId(Long addrId, String userId) {

    }

    @Override
    @Cacheable(cacheNames = "UserAddrDto", key = "#userId+':'+#addrId")
    public UserAddr getUserAddrByUserId(Long addrId, String userId) {
        if (addrId == 0) {
            return userAddrMapper.getDefaultUserAddr(userId);
        }
        return userAddrMapper.getUserAddrByUserIdAndAddrId(userId, addrId);
    }


}
