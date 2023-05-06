package com.will.shop.security.common.util;


import com.alibaba.ttl.TransmittableThreadLocal;
import com.will.shop.security.common.bo.UserInfoInTokenBO;

/**
 * @author will
 * @date 2023/05/06
 */
public class AuthUserContext {

    /**
     * TransmittableThreadLocal是一个由阿里巴巴开源的TransmittableThreadLocal扩展实现
     * 它可以在使用线程池时，将ThreadLocal变量值跨线程传递
     * 这对于异步或者线程池的情况下，需要在任务执行之前，将当前线程的上下文信息传递到任务执行的线程中
     */
    private static final ThreadLocal<UserInfoInTokenBO> USER_INFO_IN_TOKEN_HOLDER = new TransmittableThreadLocal<>();

    public static UserInfoInTokenBO get() {
        return USER_INFO_IN_TOKEN_HOLDER.get();
    }

    public static void set(UserInfoInTokenBO userInfoInTokenBo) {
        USER_INFO_IN_TOKEN_HOLDER.set(userInfoInTokenBo);
    }

    public static void clean() {
        if (USER_INFO_IN_TOKEN_HOLDER.get() != null) {
            USER_INFO_IN_TOKEN_HOLDER.remove();
        }
    }
}
