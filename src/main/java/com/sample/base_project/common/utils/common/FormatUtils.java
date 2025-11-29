package com.sample.base_project.common.utils.common;

import org.springframework.lang.Nullable;

import java.text.DecimalFormat;
import java.util.Optional;

public class FormatUtils {

    public static String formatCurrency(@Nullable String symbol, @Nullable Integer decimal, double amount) {
        return formatCurrency(symbol, decimal, amount, false);
    }

    /***
     * Formats a currency amount with optional symbol and decimal places.
     *
     * @param symbol   The currency symbol, can be null.
     * @param decimal  The number of decimal places, can be null.
     * @param amount   The amount to format.
     * @param padding  If true, ensures the minimum fraction digits are set to the specified decimal places.
     * @return A formatted currency string.
     */
    public static String formatCurrency(@Nullable String symbol, @Nullable Integer decimal, double amount, boolean padding) {
        symbol = Optional.ofNullable(symbol).orElse("");
        decimal = Optional.ofNullable(decimal).orElse(8);
        DecimalFormat df = new DecimalFormat(symbol + "#,##0");
        df.setMinimumFractionDigits(padding ? decimal : 0);
        df.setMaximumFractionDigits(decimal);
        return df.format(amount);
    }
}
