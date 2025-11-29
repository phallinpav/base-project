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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CollectionSameSize.CollectionSameSizeValidator.class)
public @interface CollectionSameSize {
    String message() default "{must.be.same.size}";

    String[] fields();
    Condition[] ifConditions() default {};
    boolean ifConditionsAnd() default true;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class CollectionSameSizeValidator implements ConstraintValidator<CollectionSameSize, Object> {
        private List<String> fields;
        private String message;
        private Condition[] ifConditions;
        private boolean ifConditionsAnd;

        @Override
        public void initialize(CollectionSameSize constraintAnnotation) {
            fields = Arrays.asList(constraintAnnotation.fields());
            message = fields + " : " + constraintAnnotation.message();
            ifConditions = constraintAnnotation.ifConditions();
            ifConditionsAnd = constraintAnnotation.ifConditionsAnd();
        }
        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
            if (fields.isEmpty()) {
                return true;
            }

            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);
            Objects.requireNonNull(beanWrapper);

            Boolean check = null;
            for (var condition : ifConditions) {
                if (ifConditionsAnd) {
                    if (!Condition.ConditionCheck.check(condition, beanWrapper)) {
                        check = false;
                        break;
                    }
                } else {
                    if (Condition.ConditionCheck.check(condition, beanWrapper)) {
                        check = true;
                        break;
                    }
                }
            }
            if (check == null) {
                check = ifConditionsAnd;
            }

            if (check) {
                List<Integer> sizeList = new ArrayList<>();
                for (String field : fields) {
                    Object propertyValue = beanWrapper.getPropertyValue(field);
                    if (propertyValue instanceof Collection<?> collection) {
                        sizeList.add(collection.size());
                    } else {
                        sizeList.add(null);
                    }
                }
                boolean isValid = sizeList.stream().distinct().count() == 1;
                if (!isValid) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
                    return false;
                }
            }
            return true;
        }
    }
}
