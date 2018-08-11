package com.krista.spring.boot.dto.annotation;


import com.krista.spring.boot.dto.enumeration.DictionaryField;

import java.lang.annotation.*;

/**
 * Description: 字典注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dictionary {

    String code() default "";

    String targetField() default "";

    DictionaryField source() default DictionaryField.DATA_DICTIONARY_NAME;
}
