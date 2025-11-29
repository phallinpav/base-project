package com.sample.base_project.common.exception;

import com.sample.base_project.common.utils.common.TranslationUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorMessageUtils {
    private static final String NOT_FOUND = "not.found";
    private static final String ALREADY_EXIST = "already.exist";
    private static final String INVALID = "invalid";
    private static final String INCORRECT = "incorrect";
    private static final String DISABLE = "disable";
    private static final String FAIL = "fail";

    public static String getTranslatedMessage(IErrorModule module, String error, Object... args) {
        String translatedModule = TranslationUtils.translate(module.getValue());
        return getTranslatedMessage(translatedModule, error, args);
    }

    public static String getTranslatedMessage(String field, String error, Object... args) {
        String translatedError = TranslationUtils.translate(error, args);
        return field + " : " + translatedError;
    }

    public static BusinessException notFound(IErrorModule module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, NOT_FOUND, args));
    }

    public static BusinessException notFound(ErrorType errorType, IErrorModule module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, NOT_FOUND, args), errorType);
    }

    public static BusinessException notFound(String module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, NOT_FOUND, args));
    }

    public static BusinessException notFound(ErrorType errorType, String module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, NOT_FOUND, args), errorType);
    }

    public static BusinessException alreadyExist(IErrorModule module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, ALREADY_EXIST, args));
    }

    public static BusinessException alreadyExist(ErrorType errorType, IErrorModule module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, ALREADY_EXIST, args), errorType);
    }

    public static BusinessException alreadyExist(String module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, ALREADY_EXIST, args));
    }

    public static BusinessException alreadyExist(ErrorType errorType, String module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, ALREADY_EXIST, args), errorType);
    }

    public static BusinessException invalid(IErrorModule module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, INVALID, args));
    }

    public static BusinessException invalid(ErrorType errorType, IErrorModule module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, INVALID, args), errorType);
    }

    public static BusinessException invalid(String module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, INVALID, args));
    }

    public static BusinessException invalid(ErrorType errorType, String module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, INVALID, args), errorType);
    }

    public static BusinessException incorrect(IErrorModule module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, INCORRECT, args));
    }

    public static BusinessException incorrect(ErrorType errorType, IErrorModule module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, INCORRECT, args), errorType);
    }

    public static BusinessException incorrect(String module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, INCORRECT, args));
    }

    public static BusinessException incorrect(ErrorType errorType, String module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, INCORRECT, args), errorType);
    }

    public static BusinessException disable(IErrorModule module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, DISABLE, args));
    }

    public static BusinessException disable(ErrorType errorType, IErrorModule module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, DISABLE, args), errorType);
    }

    public static BusinessException disable(String module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, DISABLE, args));
    }

    public static BusinessException disable(ErrorType errorType, String module, Object... args) {
        return new BusinessException(getTranslatedMessage(module, DISABLE, args), errorType);
    }

    public static BusinessException fail(ErrorActionModule action, IErrorModule module, Object... args) {
        String actionTran = "[" + TranslationUtils.translate(action.getValue()) + "] ";
        return new BusinessException(actionTran + getTranslatedMessage(module, FAIL, args));
    }

    public static BusinessException fail(ErrorType errorType, ErrorActionModule action, IErrorModule module, Object... args) {
        String actionTran = "[" + TranslationUtils.translate(action.getValue()) + "] ";
        return new BusinessException(actionTran + getTranslatedMessage(module, FAIL, args), errorType);
    }

    public static BusinessException error(String errorField, String error, Object... args) {
        return new BusinessException(getTranslatedMessage(errorField, error, args));
    }

    public static BusinessException error(ErrorType errorType, String errorField, String error, Object... args) {
        return new BusinessException(getTranslatedMessage(errorField, error, args), errorType);
    }

    public static BusinessException error(IErrorModule module, String error, Object... args) {
        return new BusinessException(getTranslatedMessage(module, error, args));
    }

    public static BusinessException error(ErrorType errorType, IErrorModule module, String error, Object... args) {
        return new BusinessException(getTranslatedMessage(module, error, args), errorType);
    }

    public static BusinessException error(String msg, Object... args) {
        return new BusinessException(TranslationUtils.translate(msg, args));
    }

    public static BusinessException error(ErrorType errorType, String msg, Object... args) {
        return new BusinessException(TranslationUtils.translate(msg, args), errorType);
    }

    public static BusinessException error(String msg, Exception e, Object... args) {
        log.error(e.getMessage(), e);
        return error(msg, args);
    }

    public static BusinessException error(ErrorType errorType, String msg, Exception e, Object... args) {
        log.error(e.getMessage(), e);
        return error(msg, args, errorType);
    }
}
