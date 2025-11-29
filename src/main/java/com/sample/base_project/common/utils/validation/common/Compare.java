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
@Constraint(validatedBy = Compare.CompareValidator.class)
public @interface Compare {
    String message() default "{must.be.different.from}";
    String[] field1();
    String[] field2();
    boolean mustBeDiff() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CompareValidator implements ConstraintValidator<Compare, Object> {

        private List<String> field1;
        private List<String> field2;
        private boolean mustBeDiff;
        private String message;

        @Override
        public void initialize(Compare constraintAnnotation) {
            field1 = Arrays.asList(constraintAnnotation.field1());
            field2 = Arrays.asList(constraintAnnotation.field2());
            message = field1 + " " + constraintAnnotation.message() + " " + field2;
            mustBeDiff = constraintAnnotation.mustBeDiff();
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);
            Objects.requireNonNull(beanWrapper);
            String value1 = this.field1.stream().map(beanWrapper::getPropertyValue).toList().toString();
            String value2 = this.field2.stream().map(beanWrapper::getPropertyValue).toList().toString();
            boolean isDiff = !value1.equals(value2);
            boolean isValid = mustBeDiff == isDiff;
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            }
            return true;
        }

    }

}
