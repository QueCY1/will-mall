package com.will.shop.sys.enums;

public enum RoleEnum {

    /**
     * 超级管理员：最高权限
     */
    SUPER_ADMIN(1),

    /**
     * 普通管理员
     */
    ADMIN(2),

    /**
     * 后台员工
     */
    GUEST(3);

    private final int value;

    RoleEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
