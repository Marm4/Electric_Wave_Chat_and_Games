package com.marm4.electric_wave.global;

import com.marm4.electric_wave.model.GroupChat;
import com.marm4.electric_wave.model.User;

public class CurrentChat {
    private User user;
    private GroupChat groupChat;
    private static CurrentChat instance;


    public static CurrentChat getInstance(){
        if(instance == null){
            instance = new CurrentChat();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GroupChat getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(GroupChat groupChat) {
        this.groupChat = groupChat;
    }

    public static void setInstance(CurrentChat instance) {
        CurrentChat.instance = instance;
    }
}
