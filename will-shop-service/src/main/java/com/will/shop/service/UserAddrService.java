package com.will.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.bean.model.UserAddr;

/**
 * @author will
 */
public interface UserAddrService extends IService<UserAddr> {
    /**
     * 获取用户默认地址
     */
    UserAddr getDefaultUserAddr(String userId);

    /**
     * 更新默认地址
     */
    void updateDefaultUserAddr(Long addrId, String userId);

    /**
     * 删除缓存
     */
    void removeUserAddrByUserId(Long addrId, String userId);

    /**
     * 根据用户id和地址id获取用户地址
     */
    UserAddr getUserAddrByUserId(Long addrId, String userId);
}

