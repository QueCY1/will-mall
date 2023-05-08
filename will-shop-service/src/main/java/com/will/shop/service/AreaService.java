package com.will.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.bean.model.Area;

import java.util.List;

/**
 * @author will
 */
public interface AreaService extends IService<Area> {

    /**
     * 通过pid 查找地址接口
     */
    List<Area> listByPid(Long pid);

    /**
     * 通过pid 清除地址缓存
     */
    void removeAreaCacheByParentId(Long pid);

}
