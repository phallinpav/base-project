package com.sample.base_project.common.utils.validation.system;

import com.sample.base_project.common.utils.validation.common.Password;
import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Password(maxLength = 20, hasSpecial = false)
@Constraint(validatedBy = {})
public @interface UserPassword {

    @OverridesAttribute(constraint = Password.class, name = "message")
    String message() default "";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
