package com.will.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.bean.model.Transcity;
import com.will.shop.bean.model.TranscityFree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author will
 */
public interface TransCityMapper extends BaseMapper<Transcity> {
    /**
     * 插入运费项中的城市
     *
     * @param transCities
     */
    void insertTransCities(@Param("transCities") List<Transcity> transCities);

    /**
     * 插入运费
     *
     * @param transCityFrees
     */
    void insertTransCityFrees(@Param("transCityFrees") List<TranscityFree> transCityFrees);

    /**
     * 根据运费id删除运费项
     *
     * @param transfeeIds
     */
    void deleteBatchByTransfeeIds(@Param("transfeeIds") List<Long> transfeeIds);

    /**
     * 根据运费金额项id删除运费金额
     *
     * @param transfeeFreeIds
     */
    void deleteBatchByTransfeeFreeIds(@Param("transfeeFreeIds") List<Long> transfeeFreeIds);

}