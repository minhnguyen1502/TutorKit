package com.example.tutorkit.Models;

public class Grade {
    private String id, type, title, date, idTutor, idStudent;
    private int grade;


    public Grade() {
    }

    public Grade(String id, String type, String title, String date, String idTutor, String idStudent, int grade) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.date = date;
        this.idTutor = idTutor;
        this.idStudent = idStudent;
        this.grade = grade;
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
