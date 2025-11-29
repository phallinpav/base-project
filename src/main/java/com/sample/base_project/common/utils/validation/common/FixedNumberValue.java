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
import java.util.HashSet;
import java.util.Set;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FixedNumberValue.FixNumberValueValidator.class)
public @interface FixedNumberValue {

    String message() default "{must.be.oneOfValues}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] values() default {};

    int[] excludedValues() default {};

    Class<?>[] classes() default {};


    class FixNumberValueValidator implements ConstraintValidator<FixedNumberValue, Number> {
        private final Set<Number> allValid = new HashSet<>();
        private String message = "";

        @Override
        public void initialize(FixedNumberValue parameters) {
            message = parameters.message();
            Class<?>[] validClasses = parameters.classes();
            int[] values = parameters.values();
            int[] excludedValue = parameters.excludedValues();

            if (values != null && values.length >= 1) {
                for (int val : values) {
                    allValid.add(val);
                }
            }
            if (validClasses != null && validClasses.length >= 1) {
                for (Class<?> clazz : validClasses) {
                    if (clazz.isEnum()) {
                        for (Object enumConstant : clazz.getEnumConstants()) {
                            addObjNumberToSet(enumConstant, allValid);
                        }
                    } else {
                        Field[] fields = clazz.getFields();
                        for (Field field : fields) {
                            if (Modifier.isStatic(field.getModifiers()) &&
                                    Modifier.isFinal(field.getModifiers())) {
                                try {
                                    Object val = field.get(null);
                                    addObjNumberToSet(val, allValid);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
            if (excludedValue != null && excludedValue.length >= 1) {
                for (int exValue : excludedValue) {
                    allValid.removeIf(val -> val.intValue() == exValue);
                }
            }
            message += " " + allValid;
        }

        @Override
        public boolean isValid(Number value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }
            boolean isValid = false;
            for (Number number : allValid) {
                // FIXME: the initialize is not yet cover double ( on exclude )
                double v1 = number.doubleValue();
                double v2 = value.doubleValue();
                if (v1 == v2) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                return false;
            }
            return true;
        }

        private void addObjNumberToSet(Object num, Set<Number> set) {
            if (num instanceof Integer val1) {
                set.add(val1);
            } else if (num instanceof Byte val1) {
                set.add(val1);
            } else if (num instanceof Long val1) {
                set.add(val1);
            } else if (num instanceof Double val1) {
                set.add(val1);
            }
        }
    }
}
