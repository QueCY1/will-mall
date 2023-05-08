package com.will.shop.sys.enums;

/**
 * 菜单类型
 * @author will
 */
public enum MenuEnum {
    /**
     * 目录
     */
    CATALOG(0),
    /**
     * 菜单
     */
    MENU(1),
    /**
     * 按钮
     */
    BUTTON(2);

    private final int value;

    MenuEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
