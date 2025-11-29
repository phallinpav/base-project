package com.sample.base_project.common.utils.validation.common;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MustBeOnCondition.MustBeOnConditionValidator.class)
public @interface MustBeOnCondition {
    String message() default ""; // NOT YET HANDLE TRANSLATION
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Condition[] mustBeConditions();
    boolean mustBeConditionsAnd() default true;
    Condition[] ifConditions();
    boolean ifConditionsAnd() default true;
    boolean oppositeCheck() default false;

    class MustBeOnConditionValidator implements ConstraintValidator<MustBeOnCondition, Object> {

        private Condition[] mustBeConditions;
        private boolean mustBeConditionsAnd;
        private Condition[] ifConditions;
        private boolean ifConditionsAnd;
        private boolean oppositeCheck;

        @Override
        public void initialize(MustBeOnCondition constraintAnnotation) {
            mustBeConditions = constraintAnnotation.mustBeConditions();
            mustBeConditionsAnd = constraintAnnotation.mustBeConditionsAnd();
            ifConditions = constraintAnnotation.ifConditions();
            ifConditionsAnd = constraintAnnotation.ifConditionsAnd();
            oppositeCheck = constraintAnnotation.oppositeCheck();
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
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
                for (var condition : mustBeConditions) {
                    if (mustBeConditionsAnd) {
                        if (!Condition.ConditionCheck.check(condition, beanWrapper)) {
                            String validationMessage = getMessageString(condition, beanWrapper);
                            context.disableDefaultConstraintViolation();
                            context.buildConstraintViolationWithTemplate(validationMessage).addConstraintViolation();
                            return false;
                        }
                    } else {
                        if (Condition.ConditionCheck.check(condition, beanWrapper)) {
                            return true;
                        }
                    }
                }
                if (mustBeConditionsAnd) {
                    return true;
                } else {
                    List<String> messages = new ArrayList<>();
                    for (var condition : mustBeConditions) {
                        String validationMessage = getMessageString(condition, beanWrapper);
                        messages.add(validationMessage);
                    }
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(String.join(mustBeConditionsAnd ? ", and " : ", or ", messages)).addConstraintViolation();
                    return false;
                }
            } else {
                if (oppositeCheck) {
                    // FIXME: the code is duplicated with above
                    for (var condition : mustBeConditions) {
                        condition = Condition.ConditionCheck.getOppositeCondition(condition);
                        if (mustBeConditionsAnd) {
                            if (!Condition.ConditionCheck.check(condition, beanWrapper)) {
                                String validationMessage = getMessageString(condition, beanWrapper);
                                context.disableDefaultConstraintViolation();
                                context.buildConstraintViolationWithTemplate(validationMessage).addConstraintViolation();
                                return false;
                            }
                        } else {
                            if (Condition.ConditionCheck.check(condition, beanWrapper)) {
                                return true;
                            }
                        }
                    }
                    if (mustBeConditionsAnd) {
                        return true;
                    } else {
                        List<String> messages = new ArrayList<>();
                        for (var condition : mustBeConditions) {
                            condition = Condition.ConditionCheck.getOppositeCondition(condition);
                            String validationMessage = getMessageString(condition, beanWrapper);
                            messages.add(validationMessage);
                        }
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(String.join(mustBeConditionsAnd ? ", and " : ", or ", messages)).addConstraintViolation();
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }

        private static String getMessageString(Condition condition, BeanWrapper beanWrapper) {
            String message = condition.field() + " : must be " + condition.operator().toMessageValidationValue();
            switch (condition.operator()) {
                case IS_EMPTY, IS_NOT_EMPTY -> {
                    return message;
                }
            }
            List<String> list = new ArrayList<>();
            for (var name : condition.valueFieldName()) {
                Object value = beanWrapper.getPropertyValue(name);
                if (value != null) {
                    list.add(value.toString());
                }
            }

            if (condition.values() != null && condition.values().length > 0) {
                list.addAll(List.of(condition.values()));
            }
            if (condition.valuesLong() != null) {
                for (var valLong : condition.valuesLong()) {
                    list.add(String.valueOf(valLong));
                }
            }
            if (condition.valueEnum() != null) {
                list.addAll(List.of(Condition.ConditionCheck.convertToEnumValues(condition.valueEnum())));
            }
            if (list.size() == 1) {
                return message + " " + list.get(0);
            } else {
                if (condition.and()){
                    return message + " " + list;
                } else {
                    return message + " one of " + list;
                }
            }
        }
    }
}
