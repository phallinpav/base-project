package com.sample.base_project.common.utils.common;


import com.sample.base_project.common.exception.ErrorMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Component
public class EncryptionUtils {
    private static String aesKey;
    private static String aesKeyFront;
    private static String aesIVFront;
    private static String aesKeyBack;
    private static String aesIVBack;

    @Value("${app.aesKey:BrH9sA4NvPqAhbRx}")
    public void setAesKey(String key) {
        if (StringUtils.isBlank(aesKey) || Objects.equals(aesKey, "BrH9sA4NvPqAhbRx")) {
            aesKey = key;
        }
    }

    @Value("${app.aeskey:BrH9sA4NvPqAhbRx}")
    public void setAeskey(String key) {
        setAesKey(key);
    }

    @Value("${app.aesKeyFront:}")
    public void setKeyFront(String key) {
        aesKeyFront = key;
    }

    @Value("${app.aesIVFront:}")
    public void setIVFront(String key) {
        aesIVFront = key;
    }

    @Value("${app.aesKeyBack:}")
    public void setKeyBack(String key) {
        aesKeyBack = key;
    }

    @Value("${app.aesIVBack:}")
    public void setIVBack(String key) {
        aesIVBack = key;
    }

    private static Cipher getCipherAES256CBC(String key, String iv, int opMode) throws Exception {
        if (key.length() != 32) {
            log.error("key length must be 32");
            throw ErrorMessageUtils.error("encryption.fail");
        }
        if (iv.length() != 16) {
            log.error("secret length must be 16");
            throw ErrorMessageUtils.error("encryption.fail");
        }
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
        SecretKey keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(opMode, keySpec, ivSpec);
        return cipher;
    }

    private static Cipher getCipherAES128ECB(String key, int opMode) throws Exception {
        if (key.length() != 16) {
            log.error("key length must be 16");
            throw ErrorMessageUtils.error("encryption.fail");
        }
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        SecretKey keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(opMode, keySpec);
        return cipher;
    }

    private static String encrypt(String sSrc, String sKey) throws Exception {
        if (StringUtils.isBlank(sSrc)) return null;
        Cipher cipher = getCipherAES128ECB(sKey, Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private static String decrypt(String sSrc, String sKey) throws Exception {
        if (StringUtils.isBlank(sSrc)) return null;
        Cipher cipher = getCipherAES128ECB(sKey, Cipher.DECRYPT_MODE);

        byte[] encrypted = Base64.getDecoder().decode(sSrc);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original, StandardCharsets.UTF_8);
    }

    private static String encrypt(String sSrc, String key, String iv) throws Exception {
        if (StringUtils.isBlank(sSrc)) return null;
        Cipher cipher = getCipherAES256CBC(key, iv, Cipher.ENCRYPT_MODE);

        byte[] encryptedBytes = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String decrypt(String sSrc, String key, String iv) throws Exception {
        if (StringUtils.isBlank(sSrc)) return null;
        Cipher cipher = getCipherAES256CBC(key, iv, Cipher.DECRYPT_MODE);

        byte[] encrypted = Base64.getDecoder().decode(sSrc);
        if (encrypted.length % 16 != 0) {
            log.error("decrypt value is not multiple of 16");
            return null;
        }
        byte[] original = cipher.doFinal(encrypted);
        return new String(original, StandardCharsets.UTF_8);
    }

    public static String encrypt(String content) {
        return encrypt(content, false);
    }
    public static String encryptFront(String content) {
        return encrypt(content, true);
    }

    private static String encrypt(String content, boolean isFront) {
        try {
            String key = isFront ? aesKeyFront : aesKeyBack;
            String iv = isFront ? aesIVFront : aesIVBack;

            if (StringUtils.isBlank(key, iv)) {
                return encrypt(content, aesKey);
            }

            return encrypt(content, key, iv);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return content;
        }
    }

    public static String decrypt(String content) {
        return decrypt(content, false);
    }

    public static String decryptFront(String content) {
        return decrypt(content, true);
    }

    private static String decrypt(String content, boolean isFront) {
        try {
            String key = isFront ? aesKeyFront : aesKeyBack;
            String iv = isFront ? aesIVFront : aesIVBack;
            if (StringUtils.isBlank(key, iv)) {
                return decrypt(content, aesKey);
            }

            return decrypt(content, key, iv);
        } catch (Exception e) {
            throw ErrorMessageUtils.error("decryption.fail");
        }
    }
}
