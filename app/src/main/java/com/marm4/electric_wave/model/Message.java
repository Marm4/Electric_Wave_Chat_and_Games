package com.marm4.electric_wave.model;

import java.util.Date;

public class Message {
    private String id;
    private User currentUser;
    private User friend;
    private String content;
    private Date timestamp;
    private Boolean friendMessage;

    public Message(String id, User receptor, User sender, String content, Date timestamp, Boolean friendMessage) {
        this.id = id;
        this.currentUser = sender;
        this.friend = receptor;
        this.content = content;
        this.timestamp = timestamp;
        this.friendMessage = friendMessage;
    }

    public String getId() {
        return id;
    }
    public User getCurrentUser() {
        return currentUser;
    }

    public User getFriend() {
        return friend;
    }
    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Boolean isFriendMessage() {
        return friendMessage;
    }
}
