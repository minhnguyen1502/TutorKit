package com.example.contact;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    private String date;
    private String email;
    private String image;

    public User(int id, String name, String date, String email, String image) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.email = email;
        this.image = image;
    }

    public User(String name, String date, String email, String image) {
        this.name = name;
        this.date = date;
        this.email = email;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
