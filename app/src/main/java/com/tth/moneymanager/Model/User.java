package com.tth.moneymanager.Model;

public class User {
    private int id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String address;
    private String image_url;
    private double remained_amount;
    private double remained_debt;

    public User(int id, String email, String password, String firstname, String lastname, String adrress, String image_url, double remained_amount, double remained_debt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = adrress;
        this.image_url = image_url;
        this.remained_amount = remained_amount;
        this.remained_debt = remained_debt;
    }

    public User(String email, String password, String firstname, String lastname, String adrress, String image_url, double remained_amount, double remained_debt) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = adrress;
        this.image_url = image_url;
        this.remained_amount = remained_amount;
        this.remained_debt = remained_debt;
    }

    public User() {
    }

    public double getRemained_debt() {
        return remained_debt;
    }

    public void setRemained_debt(double remained_debt) {
        this.remained_debt = remained_debt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAdrress() {
        return address;
    }

    public void setAdrress(String adrress) {
        this.address = adrress;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public double getRemained_amount() {
        return remained_amount;
    }

    public void setRemained_amount(double remained_amount) {
        this.remained_amount = remained_amount;
    }
}
