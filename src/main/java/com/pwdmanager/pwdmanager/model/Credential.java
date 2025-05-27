package com.pwdmanager.pwdmanager.model;

public class Credential {
    private String service;
    private String username;
    private String encryptedPassword;

    public Credential(String service, String username, String encryptedPassword) {
        this.service = service;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
