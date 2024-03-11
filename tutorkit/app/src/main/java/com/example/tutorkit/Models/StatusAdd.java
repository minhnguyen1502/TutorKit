package com.example.tutorkit.Models;

public class StatusAdd {
    private String idList;
    private Boolean status;

    public StatusAdd() {
    }


    public StatusAdd(String idList, Boolean status) {
        this.idList = idList;
        this.status = status;
    }

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
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