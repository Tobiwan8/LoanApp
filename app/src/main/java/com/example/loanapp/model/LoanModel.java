package com.example.loanapp.model;

import java.util.Date;

// Loan Model representing a single loan
public class LoanModel {
    private ItemModel item;
    private Date loanDate;

    // Constructor
    public LoanModel(ItemModel item, Date loanDate) {
        this.item = item;
        this.loanDate = loanDate;
    }

    // Getters and Setters
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