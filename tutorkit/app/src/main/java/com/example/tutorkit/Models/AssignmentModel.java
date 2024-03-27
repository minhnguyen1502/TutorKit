package com.example.tutorkit.Models;

public class AssignmentModel {
    String id, img, idSubmit, idStudent;

    public AssignmentModel(String id, String img, String idSubmit, String idStudent) {
        this.id = id;
        this.img = img;
        this.idSubmit = idSubmit;
        this.idStudent = idStudent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSubmit() {
        return idSubmit;
    }

    public void setIdSubmit(String idSubmit) {
        this.idSubmit = idSubmit;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public AssignmentModel() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
