package com.sample.base_project.core.constants;

import com.sample.base_project.core.constants.base.BaseEnum;
import lombok.Getter;

@Getter
public enum UserStatus implements BaseEnum {
    ACTIVE("A", "active"),
    DISABLE("D", "disable"),
    ;

    private final String value;
    private final String desc;

    UserStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
