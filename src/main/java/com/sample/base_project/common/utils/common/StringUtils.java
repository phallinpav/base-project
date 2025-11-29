package com.sample.base_project.common.utils.common;

public class StringUtils {

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static boolean isBlank(String... value) {
        for (String s : value) {
            if (isBlank(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotBlank(String... value) {
        return !isBlank(value);
    }

    public static String camelCaseToSnakeCase(String val) {
        StringBuilder converted = new StringBuilder();
        for (char s : val.toCharArray()) {
            if (Character.isUpperCase(s)) {
                converted.append("_").append(Character.toLowerCase(s));
            } else {
                converted.append(s);
            }
        }
        return converted.toString();
    }

    public static String snakeCaseToCamelCase(String val) {
        StringBuilder converted = new StringBuilder();
        boolean change = false;
        for (char s : val.toCharArray()) {
            if (s == '_') {
                change = true;
            } else {
                if (change) {
                    change = false;
                    converted.append(Character.toUpperCase(s));
                } else {
                    converted.append(s);
                }
            }
        }
        return converted.toString();
    }

    public static String specialConvert(String val, char delimiterFrom, String delimiterTo, ConvertCase convertCase) {
        StringBuilder converted = new StringBuilder();
        boolean change = true;
        boolean isFirst = true;
        for (char s : val.toCharArray()) {
            if (s == delimiterFrom) {
                change = true;
            } else {
                if (change) {
                    change = false;
                    if (!isFirst) {
                        converted.append(delimiterTo);
                    }
                    switch (convertCase) {
                        case ALL_UPPER, FIRST_UPPER -> converted.append(Character.toUpperCase(s));
                        case ALL_LOWER, FIRST_LOWER -> converted.append(Character.toLowerCase(s));
                        case NO_CHANGE -> converted.append(s);
                    }
                } else {
                    switch (convertCase) {
                        case ALL_UPPER -> converted.append(Character.toUpperCase(s));
                        case ALL_LOWER -> converted.append(Character.toLowerCase(s));
                        default -> converted.append(s);
                    }
                }
            }
            isFirst = false;
        }
        return converted.toString();
    }

    public enum ConvertCase {
        ALL_UPPER,
        ALL_LOWER,
        FIRST_UPPER,
        FIRST_LOWER,
        NO_CHANGE,
    }
}
