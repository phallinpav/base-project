package com.sample.base_project.common.utils.validation.system;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Digits(integer = 6, fraction = 0, message = "{must.have.specific.digit}")
@Size(min = 6, max = 6, message = "{must.have.specific.digit}")
@Constraint(validatedBy = {})
public @interface VerifyCode {

    /* currently this message, groups, payload here cannot not apply to validation, it will follow whatever validation
        annotation declare above this class
     */
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
