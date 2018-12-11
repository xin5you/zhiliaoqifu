package com.ebeijia.zl.shop.utils;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个注解用于标记禁止频繁请求的服务;
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SessionCheck {

    @AliasFor("intervalTime")
    int value() default 5;

    @AliasFor("value")
    int intervalTime() default 5;

    boolean cache() default false;
}
