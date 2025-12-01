package com.sample.base_project.common.utils.validation.system;


import com.sample.base_project.common.utils.validation.common.Password;
import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Digits(integer = 6, fraction = 0)
@Size(min = 6, max = 6)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
public @interface VerifyCode {

    @OverridesAttribute(constraint = Password.class, name = "message")
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
