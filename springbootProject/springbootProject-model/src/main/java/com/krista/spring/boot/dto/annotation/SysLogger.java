package com.krista.spring.boot.dto.annotation;

import java.lang.annotation.*;

/**
 * Description: 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogger {
    String value() default "";
}
