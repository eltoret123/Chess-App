package com.example.chessapp;

public class UserSession {
    private static UserSession instance;
    private String username;
    private String email;

    // Private constructor to prevent instantiation
    private UserSession() { }

    // Access method for the singleton instance
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void clearSession() {
        username = null;
        email = null;
        instance = null;
    }
}
