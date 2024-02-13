package com.marm4.electric_wave.model;

import java.util.ArrayList;
import java.util.List;

public class CurrentUser {

    private User user;
    private List<User> friendList;
    private List<User> requestList;

    private static CurrentUser instance;

    private CurrentUser() {
        this.friendList = new ArrayList<>();
        this.requestList = new ArrayList<>();
    }

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getFriendList() {
        return friendList;
    }

    public void addFriendList(User user) {
        friendList.add(user);
    }

    public List<User> getRequestList() {
        return requestList;
    }

    public void addRequestList(User user) {
        requestList.add(user);
    }

    public void deleteRequestList(User user){
        requestList.remove(user);
    }
}
