package com.sample.base_project.common.phonecode.service;


import com.sample.base_project.common.phonecode.constant.PhoneCode;
import com.sample.base_project.common.phonecode.dto.PhoneCodeListDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PhoneCodeServiceImpl implements PhoneCodeService {

    @Override
    @Cacheable(value = "common:phoneCodeCache", key = "#root.methodName + ':' + #root.targetClass.getLocaleLanguage()")
    public PhoneCodeListDto getPhoneCodeList() {
        List<PhoneCodeListDto.PhoneCodeDto> list = new ArrayList<>();
        for (var set : PhoneCode.getMap().entrySet()) {
            list.add(PhoneCodeListDto.of(set.getKey(), set.getValue()));
        }
        return PhoneCodeListDto.builder()
                .phoneCodeList(list.stream().sorted(Comparator.comparing(PhoneCodeListDto.PhoneCodeDto::name)).toList())
                .selectPhoneCode("855")
                .build();
    }

    public static String getLocaleLanguage() {
        return LocaleContextHolder.getLocale().getLanguage().toLowerCase();
    }
}
