package com.sample.base_project;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result<T> {
    private int code;
    private String message;


    public static <T> Result<T> error(int code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .build();
    }
}
