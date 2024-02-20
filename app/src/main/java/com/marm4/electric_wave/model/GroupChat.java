package com.marm4.electric_wave.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class GroupChat{
    private String id;
    private List<User> members;
    private List<Message> messages;

    public GroupChat() {
        members = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getMembers() {
        return members;
    }


    public void addMembers(User user) {
        this.members.add(user);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessages(Message message) {
        this.messages.add(message);
    }


    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
