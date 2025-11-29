package com.sample.base_project.common.phonecode.dto;

import com.sample.base_project.common.phonecode.constant.Country;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class PhoneCodeListDto implements Serializable {
    private List<PhoneCodeDto> phoneCodeList;
    private String selectPhoneCode;

    public record PhoneCodeDto(String phoneCode, String countryCode3, String countryCode2, String country, String name, String flagCode) implements Serializable {}

    public static PhoneCodeDto of(String key, String value) {
        String text = Country.toText(value);
        return new PhoneCodeDto(key.split("\\|")[0], value, Country.toISO2(value), text, Country.toPinYin(text), Country.toFlagCode(value));
    }
}
