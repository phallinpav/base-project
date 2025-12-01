package com.sample.base_project.common.utils.validation.system;


import com.sample.base_project.common.utils.validation.common.Password;
import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^[1-9][0-9]+$")
@Size(min = 4, max = 17)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
public @interface PhoneNum {

    @OverridesAttribute(constraint = Password.class, name = "message")
    String message() default "{invalid.phone.num}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
