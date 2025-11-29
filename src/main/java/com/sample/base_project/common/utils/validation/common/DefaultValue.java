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
@Constraint(validatedBy = DefaultValue.DefaultValueValidator.class)
public @interface DefaultValue {
    String message() default "";

    String[] fields();

    String value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class DefaultValueValidator implements ConstraintValidator<DefaultValue, Object> {

        private List<String> fields;
        private String value;

        @Override
        public void initialize(DefaultValue constraintAnnotation) {
            fields = Arrays.asList(constraintAnnotation.fields());
            value = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
            if (fields.isEmpty()) {
                return true;
            }

            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);
            Objects.requireNonNull(beanWrapper);

            fields.forEach(field -> {
                if (beanWrapper.getPropertyValue(field) == null) {
                    if (Objects.equals(beanWrapper.getPropertyType(field), String.class)) {
                        beanWrapper.setPropertyValue(field, value);
                    } else if (Objects.equals(beanWrapper.getPropertyType(field), Integer.class)) {
                        beanWrapper.setPropertyValue(field, Integer.valueOf(value));
                    } else if (Objects.equals(beanWrapper.getPropertyType(field), Long.class)) {
                        beanWrapper.setPropertyValue(field, Long.valueOf(value));
                    } else if (Objects.equals(beanWrapper.getPropertyType(field), Double.class)) {
                        beanWrapper.setPropertyValue(field, Double.valueOf(value));
                    } else if (Objects.equals(beanWrapper.getPropertyType(field), Boolean.class)) {
                        beanWrapper.setPropertyValue(field, Boolean.valueOf(value));
                    }
                }
            });

            return true;
        }

    }

}
