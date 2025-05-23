package com.pwdmanager.pwdmanager.service;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordEncryptionService {

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
