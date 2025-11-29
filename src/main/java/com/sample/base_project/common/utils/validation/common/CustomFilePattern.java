package com.sample.base_project.common.utils.validation.common;

import com.sample.base_project.common.utils.common.StringUtils;
import com.sample.base_project.common.utils.validation.constant.FileType;
import com.sample.base_project.common.utils.validation.constant.ValidationConstant;
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
@Constraint(validatedBy = CustomFilePattern.CustomFileValidator.class)
public @interface CustomFilePattern {
    String message() default "incorrect.file.format";

    // if regex has value, all validations will follow this regex and ignore all conditions below
    String regex() default "";

    // if don't want directory prefix put "-", because when blank it will set to default file type prefix
    String directoryPrefix() default "";

    String mainFormat() default "\\d{4}\\/(?:[1-9]|1[0-9])\\/(?:[1-9]|[1-3][0-9])\\/\\d{16,20}(?:\\/.*)?";
    String fileNaming() default "";
    FileType[] type() default {FileType.ALL_FILE};
    String extension() default "";

    boolean blankable() default false;
    boolean nullable() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    class CustomFileValidator implements ConstraintValidator<CustomFilePattern, String> {

        private String message, regex;
        private boolean blankable, nullable;

        @Override
        public void initialize(CustomFilePattern constraintAnnotation) {
            message = constraintAnnotation.message();
            regex = constraintAnnotation.regex();
            blankable = constraintAnnotation.blankable();
            nullable = constraintAnnotation.nullable();
            if (StringUtils.isBlank(regex)) {
                String directoryPrefix = constraintAnnotation.directoryPrefix();
                String mainFormat = constraintAnnotation.mainFormat();
                String fileNaming = constraintAnnotation.fileNaming();
                FileType[] types = constraintAnnotation.type();
                String extension = constraintAnnotation.extension();


                StringBuilder regexBuilder = new StringBuilder();

                regexBuilder.append("(?i)^");
                if (StringUtils.isNotBlank(directoryPrefix)) {
                    if (!directoryPrefix.equals("-")) {
                        regexBuilder.append(directoryPrefix);
                        regexBuilder.append("\\/");
                    }
                } else {
                    if (types != null) {
                        if (types.length == 1) {
                            switch (types[0]) {
                                case IMAGE -> regexBuilder.append(FileType.Prefix.IMAGES).append("\\/");
                                case VIDEO -> regexBuilder.append(FileType.Prefix.VIDEOS).append("\\/");
                                case DOCUMENT, OTHER_DOCUMENT -> regexBuilder.append(FileType.Prefix.DOCUMENTS).append("\\/");
                                case SUBTITLE -> regexBuilder.append(FileType.Prefix.SUBTITLES).append("\\/");
                            }
                        } else {
                            regexBuilder.append("(");
                            boolean isFirst = true;
                            for (FileType fileType : types) {
                                if (isFirst) {
                                    isFirst = false;
                                } else {
                                    regexBuilder.append("|");
                                }
                                switch (fileType) {
                                    case IMAGE -> regexBuilder.append(FileType.Prefix.IMAGES);
                                    case VIDEO -> regexBuilder.append(FileType.Prefix.VIDEOS);
                                    case DOCUMENT, OTHER_DOCUMENT ->
                                            regexBuilder.append(FileType.Prefix.DOCUMENTS);
                                    case SUBTITLE -> regexBuilder.append(FileType.Prefix.SUBTITLES);
                                }
                            }
                            regexBuilder.append(")\\/");
                        }
                    }
                }
                regexBuilder.append(mainFormat);
                regexBuilder.append(fileNaming);
                regexBuilder.append("\\.(?:");
                if (StringUtils.isBlank(extension)) {
                    boolean isFirst = true;
                    if (types != null) {
                        for (FileType fileType : types) {
                            if (isFirst) {
                                isFirst = false;
                            } else {
                                regexBuilder.append("|");
                            }
                            var ext = switch (fileType) {
                                case ALL_FILE -> ValidationConstant.FILE_EXTENSIONS;
                                case IMAGE -> ValidationConstant.IMAGE_EXTENSIONS;
                                case VIDEO -> ValidationConstant.VIDEO_EXTENSIONS;
                                case DOCUMENT -> ValidationConstant.DOCUMENT_EXTENSIONS;
                                case SUBTITLE -> ValidationConstant.SUBTITLE_EXTENSIONS;
                                case OTHER_DOCUMENT -> ValidationConstant.OTHER_DOCUMENT_EXTENSIONS;
                            };
                            regexBuilder.append(ext);
                        }
                    }
                } else {
                    regexBuilder.append(extension);
                }
                regexBuilder.append(")");
                regexBuilder.append("$");


                regex = regexBuilder.toString();
            }
        }

        @Override
        public boolean isValid(String val, ConstraintValidatorContext context) {
            if (val == null) {
                return nullable;
            } else {
                if (StringUtils.isBlank(val)) {
                    return blankable;
                }
            }

            boolean isValid = Pattern.matches(regex, val);

            if (!isValid && StringUtils.isNotBlank(message) && context != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            }
            return isValid;
        }
    }
}
