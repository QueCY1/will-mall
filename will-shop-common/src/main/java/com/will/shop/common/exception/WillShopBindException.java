package com.will.shop.common.exception;

import com.will.shop.common.response.ResponseEnum;
import com.will.shop.common.response.ServerResponseEntity;
import lombok.Getter;

import java.io.Serial;

/**
 * @author will
 */
@Getter
public class WillShopBindException extends RuntimeException{

	/**
	 *
	 */
	@Serial
	private static final long serialVersionUID = -4137688758944857209L;

	/**
	 * http状态码
	 */
	private String code;

	private Object object;

	private ServerResponseEntity<?> serverResponseEntity;

	public WillShopBindException(ResponseEnum responseEnum) {
		super(responseEnum.getMsg());
		this.code = responseEnum.getCode();
	}
	/**
	 * @param responseEnum
	 */
	public WillShopBindException(ResponseEnum responseEnum, String msg) {
		super(msg);
		this.code = responseEnum.getCode();
	}

	public WillShopBindException(ServerResponseEntity<?> serverResponseEntity) {
		this.serverResponseEntity = serverResponseEntity;
	}


	public WillShopBindException(String msg) {
		super(msg);
		this.code = ResponseEnum.SHOW_FAIL.getCode();
	}

	public WillShopBindException(String msg, Object object) {
		super(msg);
		this.code = ResponseEnum.SHOW_FAIL.getCode();
		this.object = object;
	}

}
