package com.sample.base_project.common.utils.validation.common;


import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FixedValue.FixValueValidator.class)
public @interface FixedValue {

    String message() default "{must.be.oneOfValues}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] values() default {};

    String[] excludedValues() default {};

    Class<?>[] classes() default {};

    boolean regex() default false;

    boolean customValue() default false;


    class FixValueValidator implements ConstraintValidator<FixedValue, String> {
        private final LinkedHashSet<String> allValid = new LinkedHashSet<>();
        private String message = "";
        private boolean regex;
        private boolean customValue;

        @Override
        public void initialize(FixedValue parameters) {
            message = parameters.message();
            regex = parameters.regex();
            customValue = parameters.customValue();
            Class<?>[] validClasses = parameters.classes();
            String[] values = parameters.values();
            String[] excludedValue = parameters.excludedValues();

            if (values != null && values.length >= 1) {
                allValid.addAll(Arrays.asList(values));
            }
            if (validClasses != null && validClasses.length >= 1) {
                for (Class<?> clazz : validClasses) {
                    if (clazz.isEnum()) {
                        Arrays.stream(clazz.getEnumConstants()).sorted().forEachOrdered(val -> allValid.add(val.toString()));
                    } else {
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                                if (field.getType() == String.class) {
                                    try {
                                        String fieldValue = (String) field.get(null);
                                        allValid.add(fieldValue);
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else if (field.getType() == LinkedHashMap.class) {
                                    // FIXME: THIS code here is too specific, not properly organized to consider as common
                                    try {
                                        LinkedHashMap fieldValue = (LinkedHashMap) field.get(null);
                                        fieldValue.keySet().forEach(key -> allValid.add(key.toString()));
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (excludedValue != null && excludedValue.length >= 1) {
                for (String exValue : excludedValue) {
                    allValid.remove(exValue);
                }
            }
            message += " " + allValid;
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }
            if (regex) {
                for (String pattern : allValid) {
                    pattern = pattern.replaceAll("\\{.*\\}", ".*");
                    if (value.matches(pattern)) {
                        return true;
                    }
                }
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            } else {
                if (!allValid.contains(value)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                    return false;
                }
            }
            return true;
        }
    }

}
