package com.will.shop.common.bean;

import lombok.Data;

/**
 * 阿里大鱼配置信息
 * @author will
 */
@Data
public class AliDaYu {

	private String accessKeyId;
	
	private String accessKeySecret;
	
	private String signName;
}
