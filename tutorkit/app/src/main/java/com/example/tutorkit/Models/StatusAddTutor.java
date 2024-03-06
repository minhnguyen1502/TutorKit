package com.example.tutorkit.Models;

public class StatusAddTutor {
    private String idStudent;
    private Boolean status;

    public StatusAddTutor() {
    }

    public StatusAddTutor(String idStudent, Boolean status) {
        this.idStudent = idStudent;
        this.status = status;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    // https://viblo.asia/p/lam-viec-voi-firebase-realtime-database-trong-android-ORNZq4qLK0n
    //3.1 insert data
    // vì firebase realtime chỉ chấp nhận String,long,double, boolean,Object. k cho lưu mảng
    // (idStudent khi bên Student bấm chọn thì bên Tutor sẽ nhận lưu lại id cảu Student đó)
    // nên tạo object StatusAddTutor
    // rồi đẩy cái object này lên chứ kp listString idStudent

}