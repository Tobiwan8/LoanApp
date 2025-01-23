package com.example.loanapp.model;

import java.util.ArrayList;
import java.util.List;

// User Model for loan details
public class UserModel {
    private int loanNumber; // Auto-generated
    private String fullName;
    private String phoneNumber;
    private String email;
    private List<LoanModel> loans;

    // Constructor
    public UserModel(int loanNumber, String fullName, String phoneNumber, String email) {
        this.loanNumber = loanNumber;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.loans = new ArrayList<>(); // Initialize empty loan list
    }

    // Getters and Setters
    public int getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(int loanNumber) {
        this.loanNumber = loanNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<LoanModel> getLoans() {
        return loans;
    }

    public void setLoans(List<LoanModel> loans) {
        this.loans = loans;
    }
}