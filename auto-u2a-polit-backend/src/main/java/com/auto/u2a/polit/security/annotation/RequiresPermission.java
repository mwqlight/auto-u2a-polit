package com.auto.u2a.polit.security.annotation;

import java.lang.annotation.*;

/**
 * 权限注解
 * 用于方法级别的权限控制
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {
    
    /**
     * 权限编码
     */
    String value();
    
    /**
     * 权限名称
     */
    String name() default "";
    
    /**
     * 权限描述
     */
    String description() default "";
}