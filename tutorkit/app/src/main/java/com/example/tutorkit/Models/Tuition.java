package com.example.tutorkit.Models;

public class Tuition {
    private String ID,name, dateline, idStudent, idTutor;

    private int amount, price;
    public Tuition() {
    }

    public Tuition(String ID, String name, String dateline, String idStudent, String idTutor, int amount, int price) {
        this.ID = ID;
        this.name = name;
        this.dateline = dateline;
        this.idStudent = idStudent;
        this.idTutor = idTutor;
        this.amount = amount;
        this.price = price;
    }

//    public Tuition(String ID, String name, String dateline, String idStudent, int amount, int price) {
//        this.ID = ID;
//        this.name = name;
//        this.dateline = dateline;
//        this.idStudent = idStudent;
//        this.amount = amount;
//        this.price = price;
//    }


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

}
