package com.example.tutorkit.Models;

public class SubmitAssignment {
    private String title, dateline;

    public SubmitAssignment() {
    }

    public SubmitAssignment(String title, String dateline) {
        this.title = title;
        this.dateline = dateline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
}
