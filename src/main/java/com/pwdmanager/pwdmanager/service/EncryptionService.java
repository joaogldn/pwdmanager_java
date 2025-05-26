package com.pwdmanager.pwdmanager.service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionService {
    private static final String CHAVE_SECRETA = "SenhaSuperSecreta123"; //  Essa chave é um exemplo. Em produção, ela deve vir de uma variável de ambiente e ter exatamente 16, 24 ou 32 caracteres (AES-128, AES-192, AES-256).
    private static final String ALGORITHM = "AES";

    public String criptografar(String dado) throws Exception {
        SecretKeySpec chave = new SecretKeySpec(CHAVE_SECRETA.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        byte[] encryptedBytes = cipher.doFinal(dado.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String descriptografar(String dadoCriptografado) throws Exception {
        SecretKeySpec chave = new SecretKeySpec(CHAVE_SECRETA.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, chave);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(dadoCriptografado));
        return new String(decryptedBytes);
    }
}
