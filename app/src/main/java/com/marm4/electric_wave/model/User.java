package com.marm4.electric_wave.model;

public class User {
    private String id;
    private String name;
    private String userName;


    public User(String id, String name, String userName){
        this.id = id;
        this.name = name;
        this.userName = userName;
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

}
