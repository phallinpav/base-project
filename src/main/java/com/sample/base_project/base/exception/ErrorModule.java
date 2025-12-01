package com.sample.base_project.base.exception;

import com.sample.base_project.common.exception.IErrorModule;
import lombok.Getter;

@Getter
public enum ErrorModule implements IErrorModule {
    USER("user"),
    ACCOUNT("account"),
    ;
    private final String value;

    ErrorModule(String value) {
        this.value = value;
    }

}
