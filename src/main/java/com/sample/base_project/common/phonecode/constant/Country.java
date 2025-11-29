package com.sample.base_project.common.phonecode.constant;

import com.sample.base_project.common.utils.common.ObjectUtils;
import com.sample.base_project.common.utils.common.StringUtils;
import lombok.Getter;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Country {

    @Getter
    private static final LinkedHashMap<String, CountryValue> map = new LinkedHashMap<>();

    @Autowired
    public void init() {
        ClassPathResource resource = new ClassPathResource("country/country.csv");
        try (InputStream inputStream = resource.getInputStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split("\\|");
                    if (split.length == 5) {
                        // ISO3, ISO2, EN, ZH, FlagCode
                        map.put(split[0], new CountryValue(split[0], split[1], split[2], split[3], split[4]));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CountryDto getCountryDto(String code) {
        return ObjectUtils.getNullSafe(map.get(code), CountryValue::toDto);
    }

    public static CountryValue getCountryValue(String code) {
        return map.get(code);
    }

    public record CountryValue(String iso3, String iso2, String english, String chinese, String flagCode) {
        public String getNameByLang() {
            String lang = LocaleContextHolder.getLocale().getLanguage();
            if (lang.equals("zh")) {
                return chinese;
            } else {
                return english;
            }
        }
        public CountryDto toDto() {
            return CountryDto.of(this);
        }
    }

    public record CountryDto(String iso3, String iso2, String name, String flagCode) {
        public static CountryDto of(CountryValue value) {
            return new CountryDto(value.iso3(), value.iso2(), value.getNameByLang(), value.flagCode());
        }
    }

    public static String toText(String code) {
        if (StringUtils.isBlank(code)) {
            return code;
        }
        return ObjectUtils.getNullSafe(map.get(code), CountryValue::getNameByLang);
    }

    public static String toISO2(String code) {
        if (StringUtils.isBlank(code)) {
            return code;
        }
        return ObjectUtils.getNullSafe(map.get(code), CountryValue::iso2);
    }

    public static String toFlagCode(String code) {
        if (StringUtils.isBlank(code)) {
            return code;
        }
        return ObjectUtils.getNullSafe(map.get(code), CountryValue::flagCode);
    }

    public static String toCode(String value) {
        return map.entrySet().stream().filter(set -> {
            String lang = LocaleContextHolder.getLocale().getLanguage();
            if (lang.equals("zh")) {
                return set.getValue().chinese().equals(value);
            } else {
                return set.getValue().english().equals(value);
            }
        }).findAny().map(Map.Entry::getKey).orElse(null);
    }

    public static String toPinYin(String value) {
        try {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            return PinyinHelper.toHanYuPinyinString(value, format, " ", true);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw new RuntimeException(e);
        }
    }

    public <T> String translated(T code) {
        return toText(String.valueOf(code));
    }
}
