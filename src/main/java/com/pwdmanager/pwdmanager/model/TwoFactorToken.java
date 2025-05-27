package com.pwdmanager.pwdmanager.model;

import java.time.LocalDateTime;

public class TwoFactorToken {
    private String token;
    private LocalDateTime expiresAt;

    public TwoFactorToken(String token, LocalDateTime expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
