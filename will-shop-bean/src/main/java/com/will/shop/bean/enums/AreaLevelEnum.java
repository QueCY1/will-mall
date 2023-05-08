package com.will.shop.bean.enums;

/**
 * 地区层级
 *
 * @author will
 */
public enum AreaLevelEnum {

    /**
     * 省
     */
    FIRST_LEVEL(1),

    /**
     * 市
     */
    SECOND_LEVEL(2),

    /**
     * 县
     */
    THIRD_LEVEL(3);

    private final Integer num;

    public Integer value() {
        return num;
    }

    AreaLevelEnum(Integer num) {
        this.num = num;
    }

//    public static AreaLevelEnum instance(Integer value) {
//        AreaLevelEnum[] enums = values();
//        for (AreaLevelEnum statusEnum : enums) {
//            if (statusEnum.value().equals(value)) {
//                return statusEnum;
//            }
//        }
//        return null;
//    }
}