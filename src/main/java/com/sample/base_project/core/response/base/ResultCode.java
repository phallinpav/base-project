package com.sample.base_project.core.response.base;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS("200", "success"),
    BAD_REQUEST("400", "bad.request"),
    UNAUTHORIZE("401", "unauthorize"),
    FORBIDDEN("403", "forbidden"),
    NOT_FOUND("404", "not.found"),
    UNEXPECTED_ERROR("E0001", "unexpected.error"),
    ;

    private final String code;
    private final String desc;

    ResultCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
