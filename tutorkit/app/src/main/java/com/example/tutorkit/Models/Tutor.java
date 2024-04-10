package com.example.tutorkit.Models;

public class Tutor {
    private String id, name, DOB, address, phone, gender, subject, introduction, img;
    private StatusAdd statusAdd;

    public Tutor() {
    }

    public Tutor(String id, String name, String DOB, String address, String phone, String gender, String subject, String introduction, String img, StatusAdd statusAdd) {
        this.id = id;
        this.name = name;
        this.DOB = DOB;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.subject = subject;
        this.introduction = introduction;
        this.img = img;
        this.statusAdd = statusAdd;
    }

    public Tutor(String id, String name, String DOB, String address, String phone, String gender, String subject, String introduction, String img) {
        this.id = id;
        this.name = name;
        this.DOB = DOB;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.subject = subject;
        this.introduction = introduction;
        this.img = img;
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

    public StatusAdd getStatusAdd() {
        return statusAdd;
    }

    public void setStatusAdd(StatusAdd statusAdd) {
        this.statusAdd = statusAdd;
    }

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
