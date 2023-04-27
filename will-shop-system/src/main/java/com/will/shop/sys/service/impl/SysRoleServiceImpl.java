package com.will.shop.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.sys.dao.SysRoleMapper;
import com.will.shop.sys.model.SysRole;
import com.will.shop.sys.service.SysRoleService;

import java.util.List;

public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Override
    public void deleteBatch(Long[] roleIds) {

    }

    @Override
    public void saveRoleAndRoleMenu(SysRole role) {

    }

    @Override
    public void updateRoleAndRoleMenu(SysRole role) {

    }

    @Override
    public List<Long> listRoleIdByUserId(Long userId) {
        return null;
    }
}
