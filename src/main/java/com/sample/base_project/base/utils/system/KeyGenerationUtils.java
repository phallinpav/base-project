package com.sample.base_project.base.utils.system;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerationUtils {

    public static String generateKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        String key = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        key = key.replaceAll("[^a-zA-Z0-9]", "A");
        return key.substring(0, length);
    }

}
