package com.sample.base_project.common.utils.validation.common;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MustBeDiff.MustBeDiffValidator.class)
public @interface MustBeDiff {
    String message() default "{must.be.different}";

    String[] fields();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class MustBeDiffValidator implements ConstraintValidator<MustBeDiff, Object> {

        private List<String> fields;
        private String message;

        @Override
        public void initialize(MustBeDiff constraintAnnotation) {
            fields = Arrays.asList(constraintAnnotation.fields());
            message = fields + " : " + constraintAnnotation.message();
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
            if (fields.size() <= 1) {
                return true;
            }

            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);
            Objects.requireNonNull(beanWrapper);
            boolean isValid = this.fields.stream().map(beanWrapper::getPropertyValue).distinct().count() == this.fields.size();
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            }
            return true;
        }

    }

}
