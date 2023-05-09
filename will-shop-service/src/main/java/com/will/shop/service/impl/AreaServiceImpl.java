package com.will.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.Area;
import com.will.shop.service.AreaService;
import com.will.shop.dao.AreaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    private final AreaMapper areaMapper;

    @Override
    @Cacheable(cacheNames = "area", key = "#pid")
    public List<Area> listByPid(Long pid) {
        return areaMapper.selectList(new LambdaQueryWrapper<Area>().eq(Area::getParentId, pid));
    }

    @Override
    @CacheEvict(cacheNames = "area", key = "#pid")
    public void removeAreaCacheByParentId(Long pid) {

    }
}
