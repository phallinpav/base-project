package com.sample.base_project.common.utils.validation.common;

import com.sample.base_project.common.utils.common.StringUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SortableFields.SortableFieldValidator.class)
public @interface SortableFields {
    String message() default "{must.be.oneOfValues}";

    String[] fields() default {"field", "fields"};
    String[] values() default {};
    String[] excludedValues() default {};
    Class<?>[] classes() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class SortableFieldValidator implements ConstraintValidator<SortableFields, Object> {

        private List<String> fields;
        private String message;
        private final LinkedHashSet<String> allValid = new LinkedHashSet<>();

        @Override
        public void initialize(SortableFields parameters) {
            fields = Arrays.asList(parameters.fields());

            message = parameters.message();
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
                        List<Field> fields = getAllDeclaredFields(clazz);
                        setAllValid(fields, null);
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

        private void setAllValid(List<Field> fields, String prefix) {
            for (Field field : fields) {
                field.setAccessible(true);
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                    if (field.getAnnotation(Transient.class) == null) {
                        Class<?> fieldType = field.getType();

                        if (fieldType.getAnnotation(Entity.class) != null) {
                            List<Field> subFields = getAllDeclaredFields(fieldType);
                            String fieldName = field.getName();
                            if (StringUtils.isNotBlank(prefix)) {
                                fieldName = prefix + "." + fieldName;
                            }
                            setAllValid(subFields, fieldName);
                        } else {
                            String fieldName = field.getName();
                            if (StringUtils.isNotBlank(prefix)) {
                                fieldName = prefix + "." + fieldName;
                            }
                            allValid.add(fieldName);
                        }
                    }
                }
            }
        }

        private List<Field> getAllDeclaredFields(Class<?> clazz) {
            List<Field> fields = new ArrayList<>();
            while (clazz != null) { // Traverse up the class hierarchy
                fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
                clazz = clazz.getSuperclass();
            }
            return fields;
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
            if (fields.isEmpty()) {
                return true;
            }

            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);
            Objects.requireNonNull(beanWrapper);
            for (String field : fields) {
                Object value = beanWrapper.getPropertyValue(field);
                if (value == null) {
                    continue;
                }
                if (value instanceof Collection<?> col) {
                    int i = 0;
                    for (var val : col) {
                        if (!allValid.contains(String.valueOf(val))) {
                            String errMsg = "%s[%d]".formatted(field, i) + " : " + message;
                            context.disableDefaultConstraintViolation();
                            context.buildConstraintViolationWithTemplate(errMsg).addConstraintViolation();
                            return false;
                        }
                        i++;
                    }
                } else {
                    if (!allValid.contains(String.valueOf(value))) {
                        String errMsg = field + " : " + message;
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(errMsg).addConstraintViolation();
                        return false;
                    }
                }
            }
            return true;
        }
    }

}
