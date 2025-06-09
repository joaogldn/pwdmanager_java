package com.pwdmanager.pwdmanager.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIALS = "!@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?";

    /**
     * Gera uma senha segura com comprimento especificado
     * @param length Tamanho da senha (mínimo 12)
     * @return Senha gerada
     * @throws IllegalArgumentException Se o comprimento for menor que 12
     */
    public static String generateSecurePassword(int length) {
        validatePasswordLength(length);
        
        SecureRandom random = new SecureRandom();
        List<Character> passwordChars = buildPasswordChars(random, length);
        
        return convertToString(passwordChars);
    }

    /**
     * Gera uma senha segura com comprimento padrão (14 caracteres)
     */
    public static String generateSecurePassword() {
        return generateSecurePassword(14);
    }

    /**
     * Calcula a força da senha (0-5)
     * @param password Senha a ser avaliada
     * @return Nível de força
     */
    public static int calculatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        int strength = 0;
        
        // Critérios de força
        if (password.length() >= 12) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*\\d.*")) strength++;
        if (password.matches(".*[" + SPECIALS + "].*")) strength++;
        
        return strength;
    }

    public static boolean isPasswordStrong(String password) {
        return calculatePasswordStrength(password) >= 4;
    }

    private static void validatePasswordLength(int length) {
        if (length < 12) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 12 caracteres");
        }
    }

    private static List<Character> buildPasswordChars(SecureRandom random, int length) {
        List<Character> chars = new ArrayList<>(length);
        
        // Garante pelo menos um de cada tipo
        chars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        chars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        chars.add(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        chars.add(SPECIALS.charAt(random.nextInt(SPECIALS.length())));
        
        // Preenche o resto
        String allChars = LOWERCASE + UPPERCASE + NUMBERS + SPECIALS;
        for (int i = 4; i < length; i++) {
            chars.add(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        Collections.shuffle(chars, random);
        return chars;
    }

    private static String convertToString(List<Character> chars) {
        StringBuilder sb = new StringBuilder(chars.size());
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }
}
