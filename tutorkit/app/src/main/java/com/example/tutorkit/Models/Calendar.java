package com.example.tutorkit.Models;

public class Calendar {
    private String ID, name, time;

    public Calendar() {
    }

    public Calendar(String ID,String name, String time) {
        this.ID = ID;
        this.name = name;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
