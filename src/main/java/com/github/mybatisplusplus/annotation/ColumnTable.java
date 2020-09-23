package com.github.mybatisplusplus.annotation;

import java.lang.annotation.*;

/**
 * @author niuzhenhao
 * @date 2020/9/23 18:17
 * @desc
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface ColumnTable {
    String table() default "";
}
