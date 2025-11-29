package com.sample.base_project.common.utils.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Slf4j
public class TranslationUtils {
    protected static MessageSource messageSource;

    @Autowired
    public void init(MessageSource source) {
        messageSource = source;
    }

    public static String translate(String key) {
        return translate(key, new Object[]{});
    }

    public static String translateWithLang(String key, Locale language, Object... args) {
        if (StringUtils.isBlank(key)) {
            return key;
        }
        try {
            return messageSource.getMessage(key, args, language);
        } catch (Exception e) {
            log.warn("fail to translate key : {}", key);
            log.warn(e.getMessage());
            return key;
        }
    }

    public static String translateWithLang(String key, String language, Object... args) {
        return translateWithLang(key, Locale.forLanguageTag(language), args);
    }

    public static String translate(String key, Object... args) {
        return translateWithLang(key, LocaleContextHolder.getLocale(), args);
    }
}
