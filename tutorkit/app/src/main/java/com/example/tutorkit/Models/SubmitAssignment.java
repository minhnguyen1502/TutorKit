package com.example.tutorkit.Models;

public class SubmitAssignment {
    private String id,title, dateline,idTutor, idStudent, name;

    public SubmitAssignment() {
    }

    public SubmitAssignment(String id, String title, String dateline, String idTutor, String idStudent, String name) {
        this.id = id;
        this.title = title;
        this.dateline = dateline;
        this.idTutor = idTutor;
        this.idStudent = idStudent;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
