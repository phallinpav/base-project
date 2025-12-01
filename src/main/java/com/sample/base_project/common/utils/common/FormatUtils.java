package com.sample.base_project.common.utils.common;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;

public class FormatUtils {

    public static String formatCurrency(@Nullable String symbol, @Nullable Integer decimal, BigDecimal amount) {
        return formatCurrency(symbol, decimal, amount, false);
    }

    public static String formatCurrency(@Nullable String symbol, @Nullable Integer decimal, double amount) {
        return formatCurrency(symbol, decimal, amount, false);
    }

    public static String formatCurrency(@Nullable String symbol, @Nullable Integer decimal, double amount, boolean padding) {
        symbol = Optional.ofNullable(symbol).orElse("");
        decimal = Optional.ofNullable(decimal).orElse(2);
        DecimalFormat df = new DecimalFormat(symbol + "#,##0");
        df.setMinimumFractionDigits(padding ? decimal : 0);
        df.setMaximumFractionDigits(decimal);
        return df.format(amount);
    }

    public static String formatCurrency(@Nullable String symbol, @Nullable Integer decimal, BigDecimal amount, boolean padding) {
        symbol = Optional.ofNullable(symbol).orElse("");
        decimal = Optional.ofNullable(decimal).orElse(2);
        DecimalFormat df = new DecimalFormat(symbol + "#,##0");
        df.setMinimumFractionDigits(padding ? decimal : 0);
        df.setMaximumFractionDigits(decimal);
        return df.format(amount);
    }
}
