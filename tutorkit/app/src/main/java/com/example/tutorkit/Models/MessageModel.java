package com.example.tutorkit.Models;

public class MessageModel {
    String message;
    String senderid;


    public MessageModel() {
    }

    public MessageModel(String message, String senderid) {
        this.message = message;
        this.senderid = senderid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

}
