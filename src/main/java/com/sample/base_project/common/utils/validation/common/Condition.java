package com.sample.base_project.common.utils.validation.common;

import com.sample.base_project.common.exception.ErrorMessageUtils;
import com.sample.base_project.common.utils.common.StringUtils;
import com.sample.base_project.common.utils.filter.FieldTypeEnum;
import com.sample.base_project.common.utils.filter.OperatorTypeEnum;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Condition.ConditionValidator.class)
public @interface Condition {
    String message() default ""; // NOT YET HANDLE TRANSLATION
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String field() default "";
    FieldTypeEnum type() default FieldTypeEnum.TEXT;
    OperatorTypeEnum operator();
    String[] valueFieldName() default {};
    String[] values() default {};
    long[] valuesLong() default {};
    boolean and() default true;
    Class<? extends ConditionEnum> valueEnum() default ConditionEnum.class;

    interface ConditionEnum {
        default String[] getValues() {
            return new String[0];
        }
    }

    /**
     * SPECIAL CASE: if FieldTypeEnum = DATETIME or DATE
     * when values="now" it will consider value as NOW() = get current time
     */

    class ConditionValidator implements ConstraintValidator<Condition, Object> {
        private Condition condition;
        private String message;

        @Override
        public void initialize(Condition condition) {
            this.condition = condition;
            String[] valueEnums;
            valueEnums = ConditionCheck.convertToEnumValues(condition.valueEnum());
            message = condition.message();
            if (StringUtils.isBlank(message)) {
                message = getMessageString(condition.type(), condition.operator(), condition.values(), condition.valuesLong(), valueEnums);
            }
        }

        @Override
        public boolean isValid(Object inputValue, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

            return ConditionCheck.fullValidation(false, inputValue, condition, null);
        }

        private static String getMessageString(FieldTypeEnum type, OperatorTypeEnum operator, String[] values, long[] valuesLong, String[] valueEnums) {
            String message = "must be " + operator.toMessageValidationValue();
            switch (operator) {
                case IS_EMPTY, IS_NOT_EMPTY -> {
                    return message;
                }
            }
            List<String> list = new ArrayList<>();
            if (values.length > 0) list.addAll(List.of(values));
            for (Long val : valuesLong) {
                list.add(val.toString());
            }
            if (valueEnums.length > 0) list.addAll(List.of(valueEnums));
            if (list.size() == 1) {
                return message + " " + list.get(0);
            } else if (list.isEmpty()) {
                switch (type) {
                    case DATE, DATETIME -> {
                        return message + " now";
                    }
                }
            }
            return message + " " + list;
        }
    }

    class ConditionCheck {
        public static boolean check(Condition condition, BeanWrapperImpl beanWrapper) {
            Object inputValue = beanWrapper.getPropertyValue(condition.field());

            return fullValidation(false, inputValue, condition, beanWrapper);
        }

        public static boolean fullValidation(boolean isSelfCheck, Object inputValue, Condition condition, @Nullable BeanWrapperImpl beanWrapper) {
            boolean and = condition.and();
            List<Function<Void, Boolean>> validations = new ArrayList<>();
            validations.add(v -> {
                switch (condition.operator()) {
                    case IS_EMPTY -> {
                        return inputValue == null;
                    }
                    case IS_NOT_EMPTY -> {
                        return inputValue != null;
                    }
                }
                if (isSelfCheck && inputValue == null) {
                    return true;
                }
                return and;
            });

            validations.add(v -> evaluateConditions(condition.values(), val -> val, inputValue, condition, and));
            validations.add(v -> evaluateConditions(convertToLongArray(condition.valuesLong()), val -> val, inputValue, condition, and));
            validations.add(v -> evaluateConditions(convertToEnumValues(condition.valueEnum()), val -> val, inputValue, condition, and));

            if (!isSelfCheck && beanWrapper != null) {
                validations.add(v -> evaluateConditions(condition.valueFieldName(), beanWrapper::getPropertyValue, inputValue, condition, and));
            }

            return validationAnd(and, validations);
        }

        private static <T> boolean evaluateConditions(T[] values, Function<T, Object> valueExtractor, Object inputValue, Condition condition, boolean and) {
            boolean result = and;
            for (var val : values) {
                Object extractedValue = valueExtractor.apply(val);
                boolean currentResult = operatorCheck(condition.operator(), condition.type(), inputValue, extractedValue);
                if (and) {
                    result = currentResult;
                    if (!result) break;
                } else {
                    result = currentResult;
                    if (result) break;
                }
            }
            return result;
        }

        private static Long[] convertToLongArray(long[] values) {
            return Arrays.stream(values).boxed().toArray(Long[]::new);
        }

        public static String[] convertToEnumValues(Class<? extends ConditionEnum> enumValue) {
            try {
                return enumValue.getDeclaredConstructor().newInstance().getValues();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                return new String[0];
            }
        }

        public static boolean validationAnd(boolean and, Function<Void, Boolean> validation) {
            return validationAnd(and, List.of(validation));
        }

        public static boolean validationAnd(boolean and, List<Function<Void, Boolean>> validations) {
            for (var val : validations) {
                if (and) {
                    if (!val.apply(null)) {
                        return false;
                    }
                } else {
                    if (val.apply(null)) {
                        return true;
                    }
                }
            }
            return and;
        }

        public static boolean operatorCheck(OperatorTypeEnum operator, FieldTypeEnum type, Object value1, Object value2) {
            if (value1 == null) {
                return operator == OperatorTypeEnum.IS_EMPTY;
            }

            String text1 = null;
            String text2 = null;
            Number number1 = null;
            Number number2 = null;
            Date date1 = null;
            Date date2 = null;
            Timestamp timestamp1 = null;
            Timestamp timestamp2 = null;
            Boolean bool1 = null;
            Boolean bool2 = null;
            // FIXME: Not yet handle enum1 collection
            String enum1 = null;
            Collection<String> enum2 = null;
            try {
                switch (type) {
                    case TEXT, ENCRYPTED -> {
                        text1 = String.valueOf(value1);
                        text2 = String.valueOf(value2);
                    }
                    case ENUM -> {
                        enum1 = String.valueOf(value1);
                        if (value2 instanceof Collection val) {
                            enum2 = val;
                        } else {
                            // FIXME: check this again
                            enum2 = Arrays.asList(String.valueOf(value2).split(","));
                        }
                    }
                    case NUMBER, DECIMAL -> {
                        number1 = convertObjectToNumber(value1);
                        number2 = convertObjectToNumber(value2);
                    }
                    case DATE -> {
                        if (value1 instanceof Date val) {
                            date1 = val;
                        } else {
                            date1 = Date.valueOf(String.valueOf(value1));
                        }
                        if (value2 instanceof Date val) {
                            date2 = val;
                        } else {
                            if (value2 == null || value2.toString().equalsIgnoreCase("now")) {
                                date2 = Date.valueOf(LocalDate.now());
                            } else {
                                date2 = Date.valueOf(String.valueOf(value2));
                            }
                        }
                    }
                    case DATETIME -> {
                        if (value1 instanceof Timestamp val) {
                            timestamp1 = val;
                        } else {
                            timestamp1 = Timestamp.valueOf(String.valueOf(value1));
                        }
                        if (value2 instanceof Timestamp val) {
                            timestamp2 = val;
                        } else {
                            if (value2 == null || value2.toString().equalsIgnoreCase("now")) {
                                timestamp2 = Timestamp.from(Instant.now());
                            } else {
                                timestamp2 = Timestamp.valueOf(String.valueOf(value2));
                            }
                        }
                    }
                    case BOOLEAN -> {
                        if (value1 instanceof Boolean val) {
                            bool1 = val;
                        } else {
                            bool1 = Boolean.valueOf(String.valueOf(value1));
                        }
                        if (value2 instanceof Boolean val) {
                            bool2 = val;
                        } else {
                            bool2 = Boolean.valueOf(String.valueOf(value2));
                        }
                    }
                }
            } catch (Exception e) {
                throw ErrorMessageUtils.error("convert.value.check.condition.fail", e);
            }
            try {
                return switch (operator) {
                    case IS -> switch (type) {
                        case TEXT, ENCRYPTED -> text1.equals(text2);
                        case ENUM -> enum2.contains(enum1);
                        case NUMBER, DECIMAL -> number1.doubleValue() == number2.doubleValue();
                        case DATE -> date1.compareTo(date2) == 0;
                        case DATETIME -> timestamp1.compareTo(timestamp2) == 0;
                        case BOOLEAN -> bool1 == bool2;
                        default -> throw ErrorMessageUtils.error("condition.not.support");
                    };
                    case IS_NOT -> switch (type) {
                        case TEXT, ENCRYPTED -> !text1.equals(text2);
                        case ENUM -> !enum2.contains(enum1);
                        case NUMBER, DECIMAL -> number1.doubleValue() != number2.doubleValue();
                        case DATE -> date1.compareTo(date2) != 0;
                        case DATETIME -> timestamp1.compareTo(timestamp2) != 0;
                        case BOOLEAN -> bool1 != bool2;
                        default -> throw ErrorMessageUtils.error("condition.not.support");
                    };
                    case CONTAIN -> switch (type) {
                        case TEXT, ENCRYPTED -> Pattern.matches("(?i).*" + Pattern.quote(text2) + ".*", text1);
                        case ENUM -> enum2.contains(enum1);
                        default -> throw ErrorMessageUtils.error("condition.not.support");
                    };
                    case NOT_CONTAIN -> switch (type) {
                        case TEXT, ENCRYPTED -> !Pattern.matches("(?i).*" + Pattern.quote(text2) + ".*", text1);
                        case ENUM -> !enum2.contains(enum1);
                        default -> throw ErrorMessageUtils.error("condition.not.support");
                    };
                    case GREATER_THAN -> switch (type) {
                        case NUMBER, DECIMAL -> number1.doubleValue() > number2.doubleValue();
                        case DATE -> date1.after(date2);
                        case DATETIME -> timestamp1.after(timestamp2);
                        default -> throw ErrorMessageUtils.error("condition.not.support");
                    };
                    case LESS_THAN -> switch (type) {
                        case NUMBER, DECIMAL -> number1.doubleValue() < number2.doubleValue();
                        case DATE -> date1.before(date2);
                        case DATETIME -> timestamp1.before(timestamp2);
                        default -> throw ErrorMessageUtils.error("condition.not.support");
                    };
                    case GREATER_THAN_EQUAL -> switch (type) {
                        case NUMBER, DECIMAL -> number1.doubleValue() >= number2.doubleValue();
                        case DATE -> date1.compareTo(date2) >= 0;
                        case DATETIME -> timestamp1.compareTo(timestamp2) >= 0;
                        default -> throw ErrorMessageUtils.error("condition.not.support");
                    };
                    case LESS_THAN_EQUAL -> switch (type) {
                        case NUMBER, DECIMAL -> number1.doubleValue() <= number2.doubleValue();
                        case DATE -> date1.compareTo(date2) <= 0;
                        case DATETIME -> timestamp1.compareTo(timestamp2) <= 0;
                        default -> throw ErrorMessageUtils.error("condition.not.support");
                    };
                    case IS_EMPTY -> false;
                    case IS_NOT_EMPTY -> true;
                };
            } catch (Exception e) {
                throw ErrorMessageUtils.error("compare.value.validation.fail", e);
            }
        }

        private static Number convertObjectToNumber(Object value) {
            if (value == null) return null;
            if (value instanceof Integer val) {
                return val;
            } else if (value instanceof Double val) {
                return val;
            } else if (value instanceof Byte val) {
                return val;
            } else if (value instanceof Long val) {
                return val;
            } else {
                return Double.valueOf(String.valueOf(value));
            }
        }

        public static Condition getOppositeCondition(Condition condition) {
            return new Condition() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return condition.annotationType();
                }

                @Override
                public String message() {
                    return condition.message();
                }

                @Override
                public Class<?>[] groups() {
                    return condition.groups();
                }

                @Override
                public Class<? extends Payload>[] payload() {
                    return condition.payload();
                }

                @Override
                public String field() {
                    return condition.field();
                }

                @Override
                public FieldTypeEnum type() {
                    return condition.type();
                }

                @Override
                public OperatorTypeEnum operator() {
                    return condition.operator().toOpposite();
                }

                @Override
                public String[] valueFieldName() {
                    return condition.valueFieldName();
                }

                @Override
                public String[] values() {
                    return condition.values();
                }

                @Override
                public long[] valuesLong() {
                    return condition.valuesLong();
                }

                @Override
                public Class<? extends ConditionEnum> valueEnum() {
                    return condition.valueEnum();
                }

                @Override
                public boolean and() {
                    return condition.and();
                }
            };
        }

    }
}
