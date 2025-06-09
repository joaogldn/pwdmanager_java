package com.pwdmanager.pwdmanager.util;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;
    private static final String SECRET_KEY = "YourSecretKey123"; // Substitua por uma chave segura

    public static String encrypt(String data) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

            byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
            byte[] combined = new byte[iv.length + encryptedData.length];
            
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Erro na criptografia", e);
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedData);
            byte[] iv = new byte[IV_LENGTH];
            byte[] encryptedBytes = new byte[combined.length - IV_LENGTH];

            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            System.arraycopy(combined, IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);

            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Erro na descriptografia", e);
        }
    }
}