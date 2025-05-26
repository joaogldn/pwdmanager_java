package com.pwdmanager.pwdmanager.model;

public class PasswordEntry {
    private int id;
    private int userId;
    private String title;
    private String username;
    private String password;
    private String service;

    public PasswordEntry() {
    }

    public PasswordEntry(int userId, String title, String username, String password, String service) {
        this.userId = userId;
        this.title = title;
        this.username = username;
        this.password = password;
        this.service = service;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return title + " (" + username + ") - " + password;
    }
}
