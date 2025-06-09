package com.pwdmanager.pwdmanager.service;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordBreachService {

    private static final String HIBP_API = "https://api.pwnedpasswords.com/range/";

    /**
     * Verifica se uma senha já apareceu em vazamentos, usando a API do Have I Been Pwned.
     * OBS: SHA-1 é utilizado exclusivamente por exigência do protocolo HIBP.
     */
    public int checkPasswordBreach(String password) throws IOException {
        String sha1Hash = getSHA1HashForHIBP(password);
        String prefix = sha1Hash.substring(0, 5);
        String suffix = sha1Hash.substring(5);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(HIBP_API + prefix);
            String response = client.execute(request, httpResponse ->
                EntityUtils.toString(httpResponse.getEntity()));

            return countMatches(response, suffix);
        }
    }

    /**
     * Gera o hash SHA-1 exigido pela API HIBP. Não use para outras finalidades.
     */
    private String getSHA1HashForHIBP(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02X", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 não disponível no ambiente", e);
        }
    }

    private int countMatches(String response, String suffix) {
        String[] lines = response.split("\n");
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts[0].equalsIgnoreCase(suffix)) {
                return Integer.parseInt(parts[1].trim());
            }
        }
        return 0;
    }
}
