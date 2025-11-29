package com.sample.base_project.base.constant;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CacheMapKeyTTL {
    public static final Map<String, Duration> CACHE_MAP = new HashMap<>();

    public static final String STATISTIC_5MIN = BaseConstant.APP_NAME + ":statistic";
    public static final String TRANSLATE_10MIN = BaseConstant.APP_NAME + ":translate";

    static {
        CACHE_MAP.put(STATISTIC_5MIN, Duration.ofMinutes(5));
        CACHE_MAP.put(TRANSLATE_10MIN, Duration.ofMinutes(10));
    }
}
