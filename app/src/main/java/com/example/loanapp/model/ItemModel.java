package com.example.loanapp.model;

// Item Model representing tablets or cables
public class ItemModel {
    private String type; // Tablet or Cable
    private String name; // Brand or Cable Type

    // Constructor
    public ItemModel(String type, String name) {
        this.type = type;
        this.name = name;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}