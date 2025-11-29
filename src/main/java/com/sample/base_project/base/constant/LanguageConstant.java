package com.sample.base_project.base.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LanguageConstant {
    public static final String DE = "de";
    public static final String EN = "en";
    public static final String EN_US = "en-US";
    public static final String ES = "es";
    public static final String FIL = "fil";
    public static final String FR = "fr";
    public static final String HI = "hi";
    public static final String ID = "id";
    public static final String JA = "ja";
    public static final String KM = "km";
    public static final String KO = "ko";
    public static final String LO = "lo";
    public static final String MS = "ms";
    public static final String MY = "my";
    public static final String RU = "ru";
    public static final String TH = "th";
    public static final String VI = "vi";
    public static final String ZH_HANS_CN = "zh-Hans-CN";
    public static final String ZH_HANT_TW = "zh-Hant-TW";

    public static final List<Map<String, String>> LANGUAGE_LIST = new ArrayList<>();

    static {
        LANGUAGE_LIST.add(Map.of("name", "Deutsch", "sign", DE));
        LANGUAGE_LIST.add(Map.of("name", "English", "sign", EN));
        LANGUAGE_LIST.add(Map.of("name", "English (US)", "sign", EN_US));
        LANGUAGE_LIST.add(Map.of("name", "Español", "sign", ES));
        LANGUAGE_LIST.add(Map.of("name", "Filipino", "sign", FIL));
        LANGUAGE_LIST.add(Map.of("name", "Français", "sign", FR));
        LANGUAGE_LIST.add(Map.of("name", "हिन्दी", "sign", HI));
        LANGUAGE_LIST.add(Map.of("name", "Bahasa Indonesia", "sign", ID));
        LANGUAGE_LIST.add(Map.of("name", "日本語", "sign", JA));
        LANGUAGE_LIST.add(Map.of("name", "ខ្មែរ", "sign", KM));
        LANGUAGE_LIST.add(Map.of("name", "한국어", "sign", KO));
        LANGUAGE_LIST.add(Map.of("name", "ລາວ", "sign", LO));
        LANGUAGE_LIST.add(Map.of("name", "Bahasa Melayu", "sign", MS));
        LANGUAGE_LIST.add(Map.of("name", "မြန်မာ", "sign", MY));
        LANGUAGE_LIST.add(Map.of("name", "Русский", "sign", RU));
        LANGUAGE_LIST.add(Map.of("name", "ไทย", "sign", TH));
        LANGUAGE_LIST.add(Map.of("name", "Tiếng Việt", "sign", VI));
        LANGUAGE_LIST.add(Map.of("name", "简体中文", "sign", ZH_HANS_CN));
        LANGUAGE_LIST.add(Map.of("name", "繁體中文", "sign", ZH_HANT_TW));
    }
}
