package com.pwdmanager.pwdmanager.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIALS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    public static String generateSecurePassword(int length) {
        if (length < 12) {
            throw new IllegalArgumentException("Senha muito curta. Mínimo 12 caracteres.");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        List<Character> passwordChars = new ArrayList<>();

        // Garante pelo menos um caractere de cada tipo
        passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        passwordChars.add(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        passwordChars.add(SPECIALS.charAt(random.nextInt(SPECIALS.length())));

        // Preenche o resto aleatoriamente
        String allChars = LOWERCASE + UPPERCASE + NUMBERS + SPECIALS;
        for (int i = 4; i < length; i++) {
            passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Embaralha para não ter padrão previsível
        Collections.shuffle(passwordChars, random);

        // Converte para String
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }
}
