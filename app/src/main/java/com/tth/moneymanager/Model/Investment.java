package com.tth.moneymanager.Model;

public class Investment {
    private int id;
    private int user_id;
    private int transaction_id;
    private double amount;
    private double monthly_roi;
    private String init_date;
    private String finish_date;
    private String name;

    public Investment(int id, int user_id, int transaction_id, double amount, double monthly_roi, String init_date, String finish_date, String name) {
        this.id = id;
        this.user_id = user_id;
        this.transaction_id = transaction_id;
        this.amount = amount;
        this.monthly_roi = monthly_roi;
        this.init_date = init_date;
        this.finish_date = finish_date;
        this.name = name;
    }

    public Investment() {
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

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getMonthly_roi() {
        return monthly_roi;
    }

    public void setMonthly_roi(double monthly_roi) {
        this.monthly_roi = monthly_roi;
    }

    public String getInit_date() {
        return init_date;
    }

    public void setInit_date(String init_date) {
        this.init_date = init_date;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
