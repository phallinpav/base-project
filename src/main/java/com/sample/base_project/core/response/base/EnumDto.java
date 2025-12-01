package com.sample.base_project.core.response.base;

import com.sample.base_project.core.constants.base.BaseEnum;

import java.io.Serializable;

public record EnumDto(String value, String desc) implements Serializable {
    public static EnumDto of(BaseEnum e) {
        return new EnumDto(e.getValue(), e.getDesc());
    }
}
