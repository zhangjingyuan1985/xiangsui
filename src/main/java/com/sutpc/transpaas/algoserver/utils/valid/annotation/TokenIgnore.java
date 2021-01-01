package com.sutpc.transpaas.algoserver.utils.valid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义方法是否需要校验Token 作者：杨湘绥
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenIgnore {

  String value() default "";

  String desc() default "";

  String version() default "1.0";

  boolean useCache() default true;

  boolean recordHistory() default true;
}
