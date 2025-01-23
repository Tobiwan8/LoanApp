package com.example.loanapp.model;

// Admin Model representing admin credentials
public class AdminModel {
    private String userName;
    private String password;

    // Constructor
    public AdminModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

