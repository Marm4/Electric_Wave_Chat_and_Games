package com.marm4.electric_wave.model;

import java.util.ArrayList;
import java.util.List;

public class CurrentUser {

    private User user;
    private List<User> friendList;

    private static CurrentUser instance;

    private CurrentUser() {
        this.friendList = new ArrayList<>();
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

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }
}
