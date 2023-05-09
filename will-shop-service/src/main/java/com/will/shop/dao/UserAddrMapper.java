package com.will.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.will.shop.bean.model.UserAddr;
import org.apache.ibatis.annotations.Param;

/**
 * @author lanhai
 */
public interface UserAddrMapper extends BaseMapper<UserAddr> {
	/**
	 * 根据用户id获取默认地址
	 * @param userId
	 * @return
	 */
	UserAddr getDefaultUserAddr(@Param("userId") String userId);

	/**
	 * 移除用户默认地址
	 * @param userId
	 */
	void removeDefaultUserAddr(@Param("userId") String userId);

	/**
	 * 将地址设置为默认地址
	 * @param addrId
	 * @param userId
	 * @return
	 */
	int setDefaultUserAddr(@Param("addrId") Long addrId, @Param("userId") String userId);

	/**
	 * 根据用户id和地址id获取地址
	 * @param userId
	 * @param addrId
	 * @return
	 */
	UserAddr getUserAddrByUserIdAndAddrId(@Param("userId") String userId, @Param("addrId") Long addrId);
}