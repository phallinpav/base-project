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
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BlankAsNullValue.BlankAsNullValidator.class)
public @interface BlankAsNullValue {
    String message() default "";

    String[] fields();

    boolean setCollectionNull() default true;
    boolean setCollectionItemNull() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class BlankAsNullValidator implements ConstraintValidator<BlankAsNullValue, Object> {

        private List<String> fields;
        private boolean setCollectionNull;
        private boolean setCollectionItemNull;

        @Override
        public void initialize(BlankAsNullValue constraintAnnotation) {
            fields = Arrays.asList(constraintAnnotation.fields());
            setCollectionNull = constraintAnnotation.setCollectionNull();
            setCollectionItemNull = constraintAnnotation.setCollectionItemNull();
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
            if (fields.isEmpty()) {
                return true;
            }

            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);
            Objects.requireNonNull(beanWrapper);

            fields.forEach(field -> {
                Object value = beanWrapper.getPropertyValue(field);
                if (value != null) {
                    if (value instanceof String val && val.trim().isEmpty()) {
                        beanWrapper.setPropertyValue(field, null);
                    } else if (value instanceof List<?> val) {
                        if (val.isEmpty()) {
                            if (setCollectionNull) {
                                beanWrapper.setPropertyValue(field, null);
                            }
                        } else {
                            if (setCollectionItemNull) {
                                for (int i = 0; i < val.size(); i++) {
                                    if (val.get(i) instanceof String str && str.trim().isEmpty()) {
                                        val.set(i, null);
                                    }
                                }
                            }
                        }
                    } else if (value instanceof Collection<?> val) {
                        if (val.isEmpty()) {
                            if (setCollectionNull) {
                                beanWrapper.setPropertyValue(field, null);
                            }
                        } else {
                            if (setCollectionItemNull) {
                                val = val.stream().map(item -> {
                                    if (item instanceof String str && str.trim().isEmpty()) {
                                        return null;
                                    }
                                    return item;
                                }).collect(Collectors.toList());
                                beanWrapper.setPropertyValue(field, val);
                            }
                        }
                    }
                }
            });
            return true;
        }

    }

}
