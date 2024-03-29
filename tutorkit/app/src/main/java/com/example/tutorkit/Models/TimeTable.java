package com.example.tutorkit.Models;

public class TimeTable {
    private String ID, name, time,idTutor, idStudent;
    private long date;

    public TimeTable() {
    }

    public TimeTable(String ID, String name, String time, String idTutor, String idStudent, long date) {
        this.ID = ID;
        this.name = name;
        this.time = time;
        this.idTutor = idTutor;
        this.idStudent = idStudent;
        this.date = date;
    }

    public String getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(String idTutor) {
        this.idTutor = idTutor;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
