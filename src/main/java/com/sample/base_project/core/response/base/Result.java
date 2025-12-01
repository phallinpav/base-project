package com.sample.base_project.core.response.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sample.base_project.common.utils.common.DateTimeUtils;
import com.sample.base_project.common.utils.common.StringUtils;
import com.sample.base_project.common.utils.common.TranslationUtils;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Result<T> {

    private String code;
    private String codeDesc;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private String timestamp;

    public static <T> Result<T> of(String code, String desc, String msg, T data) {
        String tranCodeDesc = TranslationUtils.translate(desc);
        return Result.<T>builder()
                .code(code)
                .codeDesc(tranCodeDesc)
                .message(StringUtils.defaultIfBlank(msg, tranCodeDesc))
                .data(data)
                .timestamp(DateTimeUtils.convertDateToDateTimeString(new Date()))
                .build();
    }

    public static <T> Result<T> of(ResultCode resultCode, String msg, T data) {
        return of(resultCode.getCode(), resultCode.getDesc(), msg, data);
    }

    public static <T> Result<T> of(ResultCode resultCode, String msg) {
        return of(resultCode, msg, null);
    }

    public static <T> Result<T> of(ResultCode resultCode) {
        return of(resultCode, null);
    }

    public static <T> Result<T> success(T data) {
        return of(ResultCode.SUCCESS, null, data);
    }
}
