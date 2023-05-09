package com.will.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.bean.model.CategoryProp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 在手机数码分类下，商品的规格为(内存、颜色...)，在其他类目下有不同类型的商品规格
 * @author will
 */
public interface CategoryPropMapper extends BaseMapper<CategoryProp> {

	/**
	 * 插入分类属性
	 * @param categoryId
	 * @param propIds
	 */
	void insertCategoryProp(@Param("categoryId") Long categoryId, @Param("propIds") List<Long> propIds);

	/**
	 * 根据分类id删除分类属性
	 * @param categoryId
	 */
	void deleteByCategoryId(Long categoryId);

	/**
	 * 根据属性id删除分类属性
	 * @param propId
	 */
	void deleteByPropId(Long propId);
}