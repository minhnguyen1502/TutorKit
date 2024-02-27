package com.example.tutorkit.Models;

public class Student {
    private String name, DOB, gender, address, phone, parent_phone, img;

    public Student() {
    }

    public Student(String name, String DOB, String gender, String address, String phone, String parent_phone) {
        this.name = name;
        this.DOB = DOB;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.parent_phone = parent_phone;
    }

    public Student(String name, String DOB, String gender, String address, String phone, String parent_phone, String img) {
        this.name = name;
        this.DOB = DOB;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.parent_phone = parent_phone;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getParent_phone() {
        return parent_phone;
    }

    public void setParent_phone(String parent_phone) {
        this.parent_phone = parent_phone;
    }
}
