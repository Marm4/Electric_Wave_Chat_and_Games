package com.marm4.electric_wave.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Message{
    private String id;
    private String senderId;
    private String receptorId;
    private String content;
    private Date timestamp;

    public Message(String id, String senderId, String receptorId, String content, Date timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receptorId = receptorId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Message(String senderId, String receptorId, String content, Date timestamp) {
        this.id = "null";
        this.senderId = senderId;
        this.receptorId = receptorId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Message(){}

    public void setId(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public String getSenderId() {
        return senderId;
    }

    public String getReceptorId() { return receptorId;}

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceptorId(String receptorId) {
        this.receptorId = receptorId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


}
