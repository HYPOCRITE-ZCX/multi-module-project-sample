package com_zcx_chant_crud.annotation;

import java.lang.annotation.*;

/**
 * 加密注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Encrypt {
}
