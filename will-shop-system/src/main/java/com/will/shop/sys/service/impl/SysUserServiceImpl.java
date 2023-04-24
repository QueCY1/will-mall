package com.will.shop.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.sys.dao.SysUserMapper;
import com.will.shop.sys.model.SysUser;
import com.will.shop.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public void updatePasswordByUserId(Long userId, String newPassword) {

    }

    @Override
    public void saveUserAndUserRole(SysUser user) {

    }

    @Override
    public void updateUserAndUserRole(SysUser user) {

    }

    @Override
    public void deleteBatch(Long[] userIds, Long shopId) {
        sysUserMapper.deleteBatch(userIds, shopId);
    }

    @Override
    public SysUser getByUserName(String username) {
        return null;
    }

    @Override
    public SysUser getSysUserById(Long userId) {
        return null;
    }

    @Override
    public List<String> queryAllPerms(Long userId) {
        return null;
    }
}
