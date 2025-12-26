package com.sfl.core.service.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final String AES = "AES";
    private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";

    // Function to generate AES key
    public static SecretKey generateKey(int n) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    // Function to convert base64 encoded key to SecretKey
    public static SecretKey getKeyFromBase64String(String keyStr) {
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);
    }

    // Function to encrypt a value
    public static String encrypt(String value, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Function to decrypt a value
    public static String decrypt(String encrypted, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(original);
    }
}
