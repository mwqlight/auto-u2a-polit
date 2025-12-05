package com.auto.u2a.polit.security.annotation;

import java.lang.annotation.*;

/**
 * 角色注解
 * 用于方法级别的角色控制
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRole {
    
    /**
     * 角色编码
     */
    String value();
    
    /**
     * 角色名称
     */
    String name() default "";
    
    /**
     * 是否要求所有角色都满足（AND关系）
     */
    boolean logical() default false;
}