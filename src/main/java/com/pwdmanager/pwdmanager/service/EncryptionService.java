package com.pwdmanager.pwdmanager.service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionService {
    private static final String ALGORITHM = "AES";
    private static final String ENV_KEY_NAME = "ENCRYPTION_KEY";

    private SecretKeySpec getSecretKey() throws Exception {
        String key = System.getenv(ENV_KEY_NAME);
        if (key == null || !(key.length() == 16 || key.length() == 24 || key.length() == 32)) {
            throw new IllegalArgumentException("Chave de criptografia inválida. Ela deve ter 16, 24 ou 32 caracteres.");
        }
        return new SecretKeySpec(key.getBytes(), ALGORITHM);
    }

    public String criptografar(String dado) throws Exception {
        SecretKeySpec chave = getSecretKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        byte[] encryptedBytes = cipher.doFinal(dado.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String descriptografar(String dadoCriptografado) throws Exception {
        SecretKeySpec chave = getSecretKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, chave);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(dadoCriptografado));
        return new String(decryptedBytes);
    }
}
