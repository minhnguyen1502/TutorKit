package com.example.tutorkit.Models;

public class Tutor {
    private  String  DOB, address, email, phone, gender, subject, introduction, img;

    public Tutor() {
    }

    public Tutor(String DOB, String address, String email, String phone, String gender, String subject, String introduction) {

        this.DOB = DOB;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.subject = subject;
        this.introduction = introduction;
    }

    public Tutor(String DOB, String address, String phone, String gender, String subject, String introduction) {
        this.DOB = DOB;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.subject = subject;
        this.introduction = introduction;
    }

    public Tutor(String DOB, String address, String email, String phone, String gender, String subject, String introduction, String img) {
        this.DOB = DOB;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.subject = subject;
        this.introduction = introduction;
        this.img = img;
    }
    //    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getIntro() {
        return introduction;
    }

    public void setIntro(String introduction) {
        this.introduction = introduction;
    }
}
