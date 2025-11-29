package com.sample.base_project.common.utils.common;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    /***
     * Validates if a number has a maximum number of decimal places.
     *
     * @param number The number to validate.
     * @param maxDecimalPlaces The maximum allowed decimal places.
     * @return true if the number has at most maxDecimalPlaces decimal places, false otherwise.
     */
    public static boolean validateMaxDecimalPlace(double number, int maxDecimalPlaces) {
        double scaledNumber = number;

        for (int i = 0; i < maxDecimalPlaces; i++) {
            scaledNumber *= 10;
            if (scaledNumber == Math.floor(scaledNumber)) {
                return true;
            }
        }

        return scaledNumber == Math.floor(scaledNumber);
    }

    /***
     * Rounds down a number to a specified number of decimal places.
     *
     * @param value The number to round down.
     * @param decimalPlaces The number of decimal places to round down to.
     * @return The rounded down value.
     */
    public static double roundDown(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.floor(value * scale) / scale;
    }

    public static Pair<Long, Integer> getPairLongDecimal(BigDecimal number, int targetDecimals, boolean useRealFraction) {
        // Original number scale
        int originalScale = number.stripTrailingZeros().scale();
        if (originalScale < 0) originalScale = 0;

        // Use the max of original scale or target scale
        int decimalsUsed;
        if (useRealFraction) {
            decimalsUsed = Math.max(originalScale, targetDecimals);
        } else {
            decimalsUsed = targetDecimals;
        }

        // Multiply by 10^decimalsUsed and round if necessary
        BigDecimal scaled = number.movePointRight(decimalsUsed).setScale(0, RoundingMode.DOWN);

        return Pair.of(scaled.longValueExact(), decimalsUsed);
    }

    public static Pair<Long, Integer> getPairLongDecimal(BigDecimal number, int targetDecimals) {
        return getPairLongDecimal(number, targetDecimals, false);
    }

    public static Pair<Long, Integer> getPairLongDecimal(BigDecimal number) {
        return getPairLongDecimal(number, 0, true);
    }

    public static Pair<Long, Integer> getPairLongDecimal(double number, int targetDecimals, boolean useRealFraction) {
        return getPairLongDecimal(BigDecimal.valueOf(number), targetDecimals, useRealFraction);
    }

    public static Pair<Long, Integer> getPairLongDecimal(double number, int targetDecimals) {
        return getPairLongDecimal(number, targetDecimals, false);
    }

    public static Pair<Long, Integer> getPairLongDecimal(double number) {
        return getPairLongDecimal(number, 0, true);
    }

    public static double convertToDouble(long value, int decimals) {
        return value / Math.pow(10, decimals);
    }

    public static BigDecimal convertToBigDecimal(long value, int decimals) {
        BigDecimal numberVal = new BigDecimal(value);
        BigDecimal numberDec = BigDecimal.valueOf(Math.pow(10, decimals));
        return numberVal.divide(numberDec, decimals, RoundingMode.DOWN);
    }
}
