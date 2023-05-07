package com.will.shop.common.annotation;

import java.lang.annotation.*;

/**
 * @author will
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}
