package com.sample.base_project.common.utils.validation.system;


import com.sample.base_project.common.utils.validation.common.Password;
import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^[+-](?:[0-9]|1[0-8]|0[0-9]|((0[0-9]|1[0-8]):((0[0-9])|[0-5][0-9]))?)$", message = "{invalid.zone.id.format}")
@Constraint(validatedBy = {})
public @interface ZoneIdFormat {

    @OverridesAttribute(constraint = Password.class, name = "message")
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
