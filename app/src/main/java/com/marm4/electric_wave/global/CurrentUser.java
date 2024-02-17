package com.marm4.electric_wave.global;

import com.marm4.electric_wave.model.GroupChat;
import com.marm4.electric_wave.model.User;

import java.util.ArrayList;
import java.util.List;

public class CurrentUser {

    private User user;
    private List<User> friendList;
    private List<User> requestList;
    private List<GroupChat> chatGroupList;
    private static CurrentUser instance;

    private CurrentUser() {
        this.friendList = new ArrayList<>();
        this.requestList = new ArrayList<>();
        this.chatGroupList = new ArrayList<>();
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

    public void addChatGroupList(GroupChat chat){
        chatGroupList.add(chat);
    }

    public List<GroupChat> getChatGroupList(){
        return chatGroupList;
    }

    public User getFriendById(String id){
        User userFound = null;
        for(User user : friendList){
            if(user.getId().equals(id))
                userFound = user;
        }
        return userFound;
    }

    public Boolean containsChatGroupList(GroupChat chat){
        return chatGroupList.contains(chat);
    }

    public Boolean containsFriendList(User user){
        return friendList.contains(user);
    }
}
