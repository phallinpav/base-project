package com.sample.base_project.common.utils.validation.common;


import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MustBeMoreThan.MustBeMoreThanValidator.class)
public @interface MustBeMoreThan {

    String message() default "{must.be.more.than}";

    double value();
    boolean andEqual() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?>[] classes() default {};


    class MustBeMoreThanValidator implements ConstraintValidator<MustBeMoreThan, Number> {
        private double value;
        private boolean andEqual;
        private String message = "";

        @Override
        public void initialize(MustBeMoreThan parameters) {
            value = parameters.value();
            andEqual = parameters.andEqual();
            message = parameters.message() + " " + value;
        }

        @Override
        public boolean isValid(Number inputValue, ConstraintValidatorContext context) {
            if (inputValue == null) {
                return true;
            }

            if (inputValue.doubleValue() > value || (andEqual && inputValue.doubleValue() == value)) {
                return true;
            }

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
    }

}
