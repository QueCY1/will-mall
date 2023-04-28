package com.will.shop.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.sys.dao.SysRoleMapper;
import com.will.shop.sys.dao.SysUserRoleMapper;
import com.will.shop.sys.model.SysRole;
import com.will.shop.sys.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("sysRoleService")
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;

    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        // 删除角色
        sysRoleMapper.deleteBatch(roleIds);
        // 删除角色与用户关联表
        sysUserRoleMapper.deleteBatchByRoleId(roleIds);
        // 删除角色与菜单关联表
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
