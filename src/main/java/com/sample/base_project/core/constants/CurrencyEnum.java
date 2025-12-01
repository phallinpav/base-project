package com.sample.base_project.core.constants;

import com.sample.base_project.core.constants.base.BaseEnum;
import lombok.Getter;

@Getter
public enum CurrencyEnum implements BaseEnum {
    KHR("KHR", "KHR", "៛", 2),
    USD("USD", "USD", "$", 0),
    ;

    private final String value;
    private final String desc;
    private final String symbol;
    private final int decimal;

    CurrencyEnum(String value, String desc, String symbol, int decimal) {
        this.value = value;
        this.desc = desc;
        this.symbol = symbol;
        this.decimal = decimal;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
