package com.sample.base_project.core.constants;

import com.sample.base_project.core.constants.base.BaseEnum;
import lombok.Getter;

@Getter
public enum DepositStatus implements BaseEnum {
    PENDING("P", "pending"),
    SUCCESS("S", "success"),
    FAIL("F", "fail"),
    ;

    private final String value;
    private final String desc;

    DepositStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
