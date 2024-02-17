package com.marm4.electric_wave.model;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String userName;
    private String email;



    public User(String id, String name, String userName, String email){
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
    }

    public User(String id, String name, String userName, String email, List<Message> messageList){
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
    }

    public User() {
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
