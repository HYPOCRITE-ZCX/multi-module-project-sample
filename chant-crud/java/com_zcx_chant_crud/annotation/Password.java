package com_zcx_chant_crud.annotation;

import com_zcx_chant_crud.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;


/**
 * 校验注解，校验密码格式
 */
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Password.List.class)
@Constraint(validatedBy = {PasswordValidator.class})
public @interface Password {

    String message() default "密码格式错误，必须位4-16位，并且包含数字，大写字母，小写字母，特殊字符";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Password[] value();
    }

}
