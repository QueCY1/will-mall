package com.will.shop.sys.enums;

public enum RoleEnum {

    /**
     * 超级管理员：最高权限
     */
    SUPER_ADMIN(0),

    /**
     * 普通管理员
     */
    ADMIN(1),

    /**
     * 后台员工
     */
    GUEST(2);

    private final int value;

    RoleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
