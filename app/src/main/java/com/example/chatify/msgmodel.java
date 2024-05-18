package com.example.chatify;

public class msgmodel {

    String message, sender_id;
    long timeStamp;

    public msgmodel(String message, String sender_id, long timeStamp) {
        this.message = message;
        this.sender_id = sender_id;
        this.timeStamp = timeStamp;
    }

    public msgmodel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


}
