package com.marm4.electric_wave.model;

import android.net.Uri;

public class User {
    private String id;
    private String name;
    private String userName;
    private String email;
    private String profilePictureUrl;
    private Uri profilePicture;
    private long lastUpdate;



    public User(String id, String name, String userName, String email){
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
    }

    public User(String id, String name, String userName, String email, String profilePictureUrl, long lastUpdate){
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.lastUpdate = lastUpdate;
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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public void setProfilePicture(Uri profilePicture){
        this.profilePicture = profilePicture;
    }

    public Uri getProfilePicture(){
        return this.profilePicture;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
