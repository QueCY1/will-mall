package com.will.shop.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.sys.dao.SysUserMapper;
import com.will.shop.sys.dao.SysUserRoleMapper;
import com.will.shop.sys.model.SysUser;
import com.will.shop.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("sysUserService")
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;

    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public void updatePasswordByUserId(Long userId, String newPassword) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setPassword(newPassword);
        sysUserMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserAndUserRole(SysUser user) {
        // 添加用户的创建时间
        user.setCreateTime(new Date());
        sysUserMapper.insert(user);
        if (CollUtil.isEmpty(user.getRoleIdList())) {
            return;
        }
        sysUserRoleMapper.insertUserAndUserRole(user.getUserId(), user.getRoleIdList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAndUserRole(SysUser user) {
        sysUserMapper.updateById(user);
        // 先删除用户和角色关系
        sysUserRoleMapper.deleteByUserId(user.getUserId());
        // 检测系统用户实体类使用拥有角色列表
        if (CollUtil.isEmpty(user.getRoleIdList())) {
            return;
        }
        sysUserRoleMapper.insertUserAndUserRole(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] userIds, Long shopId) {
        sysUserMapper.deleteBatch(userIds, shopId);
        sysUserRoleMapper.deleteBatchByUserId(userIds);
    }

    @Override
    public SysUser getByUserName(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public SysUser getSysUserById(Long userId) {
        return sysUserMapper.selectById(userId);
    }

    @Override
    public List<String> queryAllPerms(Long userId) {
        return sysUserMapper.queryAllPerms(userId);
    }
}
