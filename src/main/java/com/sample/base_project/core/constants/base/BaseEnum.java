package com.sample.base_project.core.constants.base;

import com.sample.base_project.common.exception.ErrorMessageUtils;

public interface BaseEnum {
    String getValue();
    String getDesc();

    static <T extends BaseEnum> T fromValue(Class<T> enumClass, String value) {
        if (enumClass.isEnum()) {
            for (var enumValue : enumClass.getEnumConstants()) {
                if (enumValue.getValue().equals(value)) {
                    return enumValue;
                }
            }
        }
        throw ErrorMessageUtils.invalid(enumClass.getName());
    }
}
