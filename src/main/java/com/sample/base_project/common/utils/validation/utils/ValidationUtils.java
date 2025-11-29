package com.sample.base_project.common.utils.validation.utils;

import com.sample.base_project.common.exception.ErrorMessageUtils;
import com.sample.base_project.common.exception.ErrorType;
import com.sample.base_project.common.utils.common.CodeGenerationUtils;
import com.sample.base_project.common.utils.common.DateTimeUtils;
import com.sample.base_project.common.utils.common.TranslationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidationUtils {
    public static Validator validator;

    @Autowired
    public void setValidator(Validator val) {
        validator = val;
    }

    public static String generateFilePattern(String extension) {
        Date date = new Date();
        String stDate = DateTimeUtils.getCustomDateFormat("yyyy/M/d", ZoneId.of("UTC")).format(date);
        long time = System.currentTimeMillis();
        String random = CodeGenerationUtils.generateCode(4);
        return "%s/%s%s.%s".formatted(stDate, time, random, extension);
    }

    public static boolean validateRegexPattern(String value, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static void validate(Object param) {
        Errors errors = new BeanPropertyBindingResult(param, "param");
        validator.validate(param, errors);

        if (errors.hasErrors() && !errors.getAllErrors().isEmpty()) {
            throw ErrorMessageUtils.error(ErrorType.VALIDATION_PARAM, getErrorMessage(errors));
        }
    }

    public static String getErrorMessage(Errors errors) {
        String msg = getErrorMessage(errors.getFieldError());
        if (msg == null) {
            msg = getErrorMessage(errors.getGlobalError());
        }
        return msg;
    }

    public static String getErrorMessage(BindException ex) {
        String msg = getErrorMessage(ex.getFieldError());
        if (msg == null) {
            msg = getErrorMessage(ex.getGlobalError());
        }
        return msg;
    }

    public static String getErrorMessage(FieldError fieldError) {
        if (fieldError != null) {
            String msg = fieldError.getField();
            if ("typeMismatch".equals(fieldError.getCode())) {
                String[] codes = fieldError.getCodes();
                String type = "";
                if (codes != null && codes.length >= 2) {
                    type = codes[codes.length - 2];
                    if (type != null) {
                        String[] splited = type.split("\\.");
                        type = splited[splited.length - 1];
                    }
                }
                if (type == null) {
                    type = "";
                }
                msg += " : " + TranslationUtils.translate("wrong.input.value.type", type.toLowerCase());
            } else{
                msg += " : " + fieldError.getDefaultMessage();
            }
            return msg;
        }
        return null;
    }

    public static String getErrorMessage(ObjectError error) {
        if (error == null) {
            return null;
        }
        return error.getDefaultMessage();
    }

}
