package com.tth.moneymanager.Model;

public class Shopping {
    private int id;
    private int user_id;
    private int item_id;
    private int transaction_id;
    private double price;
    private String date;
    private String description;

    public Shopping(int id, int user_id, int item_id, int transaction_id, double price, String date, String description) {
        this.id = id;
        this.user_id = user_id;
        this.item_id = item_id;
        this.transaction_id = transaction_id;
        this.price = price;
        this.date = date;
        this.description = description;
    }

    public Shopping() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
