package com.example.tutorkit.Models;

public class Grade {
    private String id, type, title, date;
    private int grade;

    public Grade() {
    }

    public Grade(String id, String type, String title, String date, int grade) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.date = date;
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
