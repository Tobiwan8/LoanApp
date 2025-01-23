package com.example.loanapp.model;

import java.util.Date;

// Loan Model representing a single loan
public class LoanModel {
    private int loanID;
    private ItemModel item;
    private Date loanDate;

    // Constructor
    public LoanModel(int loanID, ItemModel item, Date loanDate) {
        this.loanID = loanID;
        this.item = item;
        this.loanDate = loanDate;
    }

    // Getters and Setters
    public int getLoanId() {
        return loanID;
    }

    public void setLoanId(int loanID) {
        this.loanID = loanID;
    }

    public ItemModel getItem() {
        return item;
    }

    public void setItem(ItemModel item) {
        this.item = item;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }
}