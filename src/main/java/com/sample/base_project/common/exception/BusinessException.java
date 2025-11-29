package com.sample.base_project.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    protected ErrorType errorType = ErrorType.BUSINESS;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
}
