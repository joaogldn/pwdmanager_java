package com.pwdmanager.pwdmanager.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtils {
    private static final String KEY = "1234567890123456"; // 16 chars para AES-128

    public static String encrypt(String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);  // converte bytes para string segura
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar", e);
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(encryptedData); // decodifica string base64 para bytes
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar", e);
        }
    }

    public static void main(String[] args) {
        String senhaOriginal = "Juniorpb22";
    
        // Criptografa
        String criptografada = CryptoUtils.encrypt(senhaOriginal);
        System.out.println("Criptografada (Base64): " + criptografada);
    
        // Descriptografa
        String descriptografada = CryptoUtils.decrypt(criptografada);
        System.out.println("Descriptografada: " + descriptografada);
    }
}