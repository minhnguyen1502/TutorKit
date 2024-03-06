package com.example.tutorkit.Models;

import java.util.ArrayList;

public class Tutor {
    private String id, name, DOB, address, phone, gender, subject, introduction, img;
    private StatusAddTutor statusAddTutor;

    public Tutor() {
    }

    public Tutor(String id, String name, String DOB, String address, String phone, String gender, String subject, String introduction, String img, StatusAddTutor statusAddTutor) {
        this.id = id;
        this.name = name;
        this.DOB = DOB;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.subject = subject;
        this.introduction = introduction;
        this.img = img;
        this.statusAddTutor = statusAddTutor;
    }

    public Tutor(String name, String DOB, String address, String phone, String gender, String subject, String introduction, String img) {
        this.name = name;
        this.DOB = DOB;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.subject = subject;
        this.introduction = introduction;
        this.img = img;
    }

    public StatusAddTutor getStatusAddTutor() {
        return statusAddTutor;
    }

    public void setStatusAddTutor(StatusAddTutor statusAddTutor) {
        this.statusAddTutor = statusAddTutor;
    }
    //    public String getIdStudent() {
//        return IdStudent;
//    }
//
//    public void setIdStudent(String IdStudent) {
//        this.IdStudent = IdStudent;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

//    public Boolean getPick() {
//        return pick;
//    }
//
//    public void setPick(Boolean pick) {
//        this.pick = pick;
//    }

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

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
