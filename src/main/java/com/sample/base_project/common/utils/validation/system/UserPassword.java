package com.sample.base_project.common.utils.validation.system;

import com.sample.base_project.common.utils.validation.common.Password;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Password(message = "{user.password.rule}", maxLength = 20, hasSpecial = false)
@Constraint(validatedBy = {})
public @interface UserPassword {

    /* currently this message, groups, payload here cannot not apply to validation, it will follow whatever validation
        annotation declare above this class
     */
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
