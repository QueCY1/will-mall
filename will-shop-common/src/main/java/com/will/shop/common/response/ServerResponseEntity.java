package com.will.shop.common.response;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * 响应实体
 * @author will
 * @param <T>
 */
@Data
@Slf4j
public class ServerResponseEntity<T> implements Serializable {

    /**
     * 状态码
     */
    private String code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 版本
     */
    private String version;

    /**
     * 时间戳
     */
    private Long timestamp;

    private String sign;

    public boolean isSuccess() {
        return Objects.equals(ResponseEnum.OK.getCode(), this.code);
    }

    public boolean isFail() {
        return !Objects.equals(ResponseEnum.OK.getCode(), this.code);
    }

    public ServerResponseEntity() {
        //版本号
        this.version = "will-mall.v000001";
    }

    public ServerResponseEntity setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 带数据的成功响应
     * @param data 成功的数据
     * @return 返回的服务器响应体
     * @param <T> 数据泛型
     */
    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setData(data);
        serverResponseEntity.setCode(ResponseEnum.OK.getCode());
        return serverResponseEntity;
    }

    /**
     * 普通成功响应
     * @return 返回的服务器响应体
     * @param <T> 数据泛型
     */
    public static <T> ServerResponseEntity<T> success() {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(ResponseEnum.OK.getCode());
        serverResponseEntity.setMsg(ResponseEnum.OK.getMsg());
        return serverResponseEntity;
    }

    /**
     * 自定义的成功响应
     * @param code 自定义成功的状态码
     * @param data 成功的数据
     * @return 返回的服务器响应体
     * @param <T> 数据泛型
     */
    public static <T> ServerResponseEntity<T> success(String code, T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(code);
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> success(Integer code, T data) {
        return success(String.valueOf(code), data);
    }

    /**
     * 带错误信息的失败响应
     * @param msg 错误信息
     * @return 返回的服务器响应体
     * @param <T> 数据的泛型
     */
    public static <T> ServerResponseEntity<T> showFailMsg(String msg) {
        log.error(msg);
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(msg);
        serverResponseEntity.setCode(ResponseEnum.SHOW_FAIL.getCode());
        return serverResponseEntity;
    }

    /**
     * 响应枚举的失败响应
     * @param responseEnum 响应枚举
     * @return 返回的服务器响应体
     * @param <T> 数据的泛型
     */
    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMsg());
        serverResponseEntity.setCode(responseEnum.getCode());
        return serverResponseEntity;
    }

    /**
     * 带数据的响应枚举的失败响应
     * @param responseEnum 响应枚举
     * @param data 数据
     * @return 返回的服务器响应体
     * @param <T> 数据的泛型
     */
    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum, T data) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMsg());
        serverResponseEntity.setCode(responseEnum.getCode());
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    /**
     * 自定义的失败响应
     * @param code 自定义的失败状态码
     * @param msg 自定义的错误信息
     * @param data 数据
     * @return 返回的服务器响应体
     * @param <T> 数据的泛型
     */
    public static <T> ServerResponseEntity<T> fail(String code, String msg, T data) {
        log.error(msg);
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(msg);
        serverResponseEntity.setCode(code);
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    /**
     * 带状态码和数据的失败响应
     * @param code 失败的状态码（Integer类型）
     * @param data 数据
     * @return 返回的服务器响应体
     * @param <T> 数据的泛型
     */
    public static <T> ServerResponseEntity<T> fail(Integer code, T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(String.valueOf(code));
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    /**
     * 带状态码和错误信息的失败响应
     * @param code 失败的状态码（String类型）
     * @param msg 错误信息
     * @return 返回的服务器响应体
     * @param <T> 数据的泛型
     */
    public static <T> ServerResponseEntity<T> fail(String code, String msg) {
        return fail(code, msg, null);
    }

}
