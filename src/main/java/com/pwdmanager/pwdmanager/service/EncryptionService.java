package com.pwdmanager.pwdmanager.service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionService {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int GCM_IV_LENGTH = 12;   // bytes
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

        // Geração do IV aleatório
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = SecureRandom.getInstanceStrong();
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, chave, spec);

        byte[] criptografado = cipher.doFinal(dado.getBytes("UTF-8"));

        // Concatena IV + dados criptografados
        byte[] result = new byte[iv.length + criptografado.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(criptografado, 0, result, iv.length, criptografado.length);

        return Base64.getEncoder().encodeToString(result);
    }

    public String descriptografar(String dadoCriptografado) throws Exception {
        SecretKeySpec chave = getSecretKey();

        byte[] dados = Base64.getDecoder().decode(dadoCriptografado);

        // Extrai IV e dados criptografados
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] criptografado = new byte[dados.length - GCM_IV_LENGTH];
        System.arraycopy(dados, 0, iv, 0, GCM_IV_LENGTH);
        System.arraycopy(dados, GCM_IV_LENGTH, criptografado, 0, criptografado.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, chave, spec);

        byte[] decifrado = cipher.doFinal(criptografado);
        return new String(decifrado, "UTF-8");
    }
}
