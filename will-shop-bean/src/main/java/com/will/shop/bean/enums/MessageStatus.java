package com.will.shop.bean.enums;

/**
 * @author will
 */
public enum MessageStatus {

    /**
     * 小程序
     */
    CANCEL(0),
    RELEASE(1);

    private final Integer num;

    public Integer value() {
        return num;
    }

    MessageStatus(Integer num) {
        this.num = num;
    }
}
