package com.example.tutorkit.Models;

public class AssignmentModel {
    String name, img;

    public AssignmentModel(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public AssignmentModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
