package com.auto.u2a.polit.security.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * 用于控制数据访问范围
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    
    /**
     * 数据权限范围：ALL-全部，DEPT-本部门，SELF-仅自己，CUSTOM-自定义
     */
    String value() default "SELF";
    
    /**
     * 自定义数据权限字段名
     */
    String field() default "create_by";
}