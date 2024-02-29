package com.example.tutorkit.Models;

public class Tuition {
    private String ID,name, dateline;

    private int amount, price;
    private int total;
    public Tuition() {
    }

    public Tuition(String ID, String name, String dateline, int amount, int price, int total) {
        this.ID = ID;
        this.name = name;
        this.dateline = dateline;
        this.amount = amount;
        this.price = price;
        this.total = total;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
