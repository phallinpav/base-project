package com.sample.base_project.common.utils.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordUtils {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:'\",.<>?";

    private static final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder(10, new SecureRandom());

    public static String encode(String plainPassword) {
        if (StringUtils.isBlank(plainPassword)) return null;
        return BCRYPT.encode(plainPassword);
    }

    public static boolean isMatch(String plainPassword, String encodedPassword) {
        if (plainPassword == null || encodedPassword == null) return false;
        return BCRYPT.matches(plainPassword, encodedPassword);
    }

    public static String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        List<Character> passwordChars = new ArrayList<>(length);

        // Ensure at least one character from each category
        passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // Fill the remaining length of the password with random characters from all categories
        String allCharacters = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;
        for (int i = 4; i < length; i++) {
            passwordChars.add(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        // Shuffle the characters to ensure randomness
        Collections.shuffle(passwordChars);

        // Convert the character list to a string
        for (Character ch : passwordChars) {
            password.append(ch);
        }

        return password.toString();
    }

}
