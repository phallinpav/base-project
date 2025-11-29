package com.sample.base_project.common.utils.validation.common;

import com.sample.base_project.common.utils.common.StringUtils;
import com.sample.base_project.common.utils.validation.system.UserPassword;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Password.PasswordValidator.class)
public @interface Password {
    String message() default "";

    // if regex has value, all validations will follow this regex and ignore all conditions below
    String regex() default "";

    int minLength() default 8;

    int maxLength() default -1;

    int atLeastForEach() default 1;

    boolean hasUpper() default true;

    boolean hasLower() default true;

    boolean hasUpperOrLower() default false;

    boolean hasSpecial() default true;

    String specialChar() default "!@#$%^&*()_+\\-=\\[\\]{}|\\\\:;\"'<>,.?/";

    boolean hasNumber() default true;
    boolean noSpace() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    class PasswordValidator implements ConstraintValidator<Password, String> {

        private String message, regex, defaultRegex, defaultMessage = "";

        @Override
        public void initialize(Password constraintAnnotation) {
            message = constraintAnnotation.message();
            regex = constraintAnnotation.regex();
            if (StringUtils.isBlank(regex)) {
                int minLength = constraintAnnotation.minLength();
                int maxLength = constraintAnnotation.maxLength();
                int atLeastForEach = constraintAnnotation.atLeastForEach();
                boolean hasLower = constraintAnnotation.hasLower();
                boolean hasUpper = constraintAnnotation.hasUpper();
                boolean hasUpperOrLower = constraintAnnotation.hasUpperOrLower();
                boolean hasSpecial = constraintAnnotation.hasSpecial();
                String specialChar = constraintAnnotation.specialChar();
                boolean hasNumber = constraintAnnotation.hasNumber();
                boolean noSpace = constraintAnnotation.noSpace();

                StringBuilder regexBuilder = new StringBuilder();
                StringBuilder messageBuilder = new StringBuilder("must has ");
                if (hasLower) {
                    regexBuilder.append("(?=");
                    regexBuilder.append(".*[a-z]".repeat(atLeastForEach));
                    regexBuilder.append(")");

                    messageBuilder.append("lowercase, ");
                }
                if (hasUpper) {
                    regexBuilder.append("(?=");
                    regexBuilder.append(".*[A-Z]".repeat(atLeastForEach));
                    regexBuilder.append(")");
                    messageBuilder.append("uppercase, ");
                }
                if (hasUpperOrLower) {
                    regexBuilder.append("(?=");
                    regexBuilder.append(".*([A-Z]|[a-z])".repeat(atLeastForEach));
                    regexBuilder.append(")");
                    messageBuilder.append("letter, ");
                }
                if (hasNumber) {
                    regexBuilder.append("(?=");
                    regexBuilder.append(".*\\d".repeat(atLeastForEach));
                    regexBuilder.append(")");
                    messageBuilder.append("number, ");
                }
                if (hasSpecial) {
                    String specialRegex = ".*[" + specialChar + "]";
                    regexBuilder.append("(?=");
                    regexBuilder.append(specialRegex.repeat(atLeastForEach));
                    regexBuilder.append(")");
                    messageBuilder.append("special characters, ");
                }
                if (noSpace) {
                    regexBuilder.append("(?=\\S+$)");
                    messageBuilder.append("no space, ");
                }
                if (minLength != -1 || maxLength != -1) {
                    if (minLength != -1 && maxLength != -1) {
                        regexBuilder.append(String.format(".{%d,%d}$", minLength, maxLength));
                        messageBuilder.append(String.format("characters between %d ~ %d", minLength, maxLength));
                    } else {
                        if (minLength == -1) {
                            regexBuilder.append(String.format(".{,%d}$", maxLength));
                            messageBuilder.append(String.format("characters less than %d", maxLength));
                        } else {
                            regexBuilder.append(String.format(".{%d,}$", minLength));
                            messageBuilder.append(String.format("characters more than %d", minLength));
                        }
                    }
                }
                defaultRegex = regexBuilder.toString();
                defaultMessage = messageBuilder.toString();
            }
        }

        @Override
        public boolean isValid(String val, ConstraintValidatorContext context) {
            if (StringUtils.isBlank(val)) {
                // will return as valid when blank, so use @NotBlank on field directly to valid this condition
                return true;
            }

            boolean isValid;
            if (StringUtils.isBlank(regex)) {
                isValid = Pattern.matches(defaultRegex, val);
            } else {
                isValid = Pattern.matches(regex, val);
            }
            if (!isValid && StringUtils.isBlank(message) && context != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(defaultMessage).addConstraintViolation();
            }
            return isValid;
        }

        public static boolean isValidUserPassword(String value) {
            PasswordValidator validator = new PasswordValidator();
            Password password = UserPassword.class.getAnnotation(Password.class);
            validator.initialize(password);
            return validator.isValid(value, null);
        }
    }
}
