package com.example.tutorkit.Models;

public class Student {
    private String name, email, DOB, gender, address, phone, parent_phone;

    public Student() {
    }

    public Student(String name, String email, String DOB, String gender, String address, String phone, String parent_phone) {
        this.name = name;
        this.email = email;
        this.DOB = DOB;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.parent_phone = parent_phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
