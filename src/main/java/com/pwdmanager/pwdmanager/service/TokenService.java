package com.pwdmanager.pwdmanager.service;

public class TokenService {
    private static String currentToken;
    private static long expirationTime; // milissegundos

    public static String gerarToken() {
        int token = (int)(Math.random() * 900000) + 100000;
        currentToken = String.valueOf(token);
        expirationTime = System.currentTimeMillis() + (5 * 60 * 1000); // 5 min
        return currentToken;
    }

    public static boolean validarToken(String input) {
        if (System.currentTimeMillis() > expirationTime) {
            System.out.println("❌ Token expirado.");
            return false;
        }

        boolean valido = currentToken != null && currentToken.equals(input);
        System.out.println(valido ? "✅ Token válido!" : "❌ Token inválido.");
        return valido;
    }
}
