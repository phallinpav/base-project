package com.sample.base_project.core.config;

import com.sample.base_project.common.exception.BusinessException;
import com.sample.base_project.common.utils.validation.utils.ValidationUtils;
import com.sample.base_project.core.response.base.Result;
import com.sample.base_project.core.response.base.ResultCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomWebExceptionHandler {

    @ExceptionHandler({BindException.class})
    public Result<String> bindException(BindException ex) {
        return Result.of(ResultCode.BAD_REQUEST, ValidationUtils.getErrorMessage(ex));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<String> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getFieldErrors();
        List<String> listMsg = new ArrayList<>();
        for (FieldError fieldError : errors) {
            if (fieldError != null) {
                listMsg.add(ValidationUtils.getErrorMessage(fieldError));
            }
        }
        if (listMsg.isEmpty()) {
            ObjectError error = ex.getGlobalError();
            String msg = error.getDefaultMessage();
            return Result.of(ResultCode.BAD_REQUEST, msg);
        } else {
            return Result.of(ResultCode.BAD_REQUEST, listMsg.toString());
        }
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public Result<String> bindException(ConstraintViolationException ex) {
        List<String> list = new ArrayList<>();
        for (var constraintViolation : ex.getConstraintViolations()) {
            String[] path = constraintViolation.getPropertyPath().toString().split("\\.");
            String field = path[path.length - 1];
            String message = field + " : " + constraintViolation.getMessage();
            list.add(message);
        }

        return Result.of(ResultCode.BAD_REQUEST, list.toString());
    }

    @ExceptionHandler({BusinessException.class})
    public Result<String> handleException(BusinessException ex) {
        ResultCode code = switch (ex.getErrorType()) {
            case VALIDATION_PARAM -> ResultCode.BAD_REQUEST;
            case BUSINESS -> ResultCode.FORBIDDEN;
            case UNAUTHORIZED -> ResultCode.UNAUTHORIZE;
            case SYSTEM, OTHER -> ResultCode.UNEXPECTED_ERROR;
        };
        return Result.of(code, ex.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public Result<String> handleException(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException ex2) {
            if (ex2.getCause() instanceof SQLIntegrityConstraintViolationException ex3) {
                if (ex3.getMessage().contains("duplicate key value")) {
                    return Result.of(ResultCode.UNEXPECTED_ERROR, "entry is duplicated");
                }
            }
        }
        return Result.of(ResultCode.UNEXPECTED_ERROR, ex.getMessage());
    }

}
