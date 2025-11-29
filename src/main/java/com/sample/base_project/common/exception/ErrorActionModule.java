package com.sample.base_project.common.exception;

import lombok.Getter;

@Getter
public enum ErrorActionModule {
    UPDATE("update"),
    DELETE("delete"),
    CREATE("create"),
    GET("get"),
    SET("set"),
    LIST("list"),
    VERIFY("verify"),
    ;
    private final String value;

    ErrorActionModule(String value) {
        this.value = value;
    }

}