package com.pwdmanager.pwdmanager.util;
import java.security.SecureRandom;
import java.util.Base64;

public class SaltUtils {
    public static String getRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128 bits
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
