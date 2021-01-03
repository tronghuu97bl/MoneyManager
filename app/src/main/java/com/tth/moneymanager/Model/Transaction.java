package com.tth.moneymanager.Model;

public class Transaction {
    private int id;
    private String date;
    private double amount;
    private String type;
    private String recipient;
    private String description;
    private int user_id;

    public Transaction() {
    }

    public Transaction(int id, String date, double amount, String type, String recipient, String description, int user_id) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.recipient = recipient;
        this.description = description;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
