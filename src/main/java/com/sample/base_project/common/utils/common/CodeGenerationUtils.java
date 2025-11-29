package com.sample.base_project.common.utils.common;

import java.util.Random;

public class CodeGenerationUtils {

    public static long generateCodeNum(int count) {
        // has no 0 at the start
        // count should be less than 19 because of long value max limit
        double pow = Math.pow(10, count - 1);
        long max = Double.valueOf(9 * pow).longValue();
        long increment = Double.valueOf(1 * pow).longValue();
        return new Random().nextLong(max) + increment;
    }

    public static String generateCode(int count) {
        return String.valueOf(generateCodeNum(count));
    }

    // has 0 at the start
    public static String getRandom6Digit() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

}
