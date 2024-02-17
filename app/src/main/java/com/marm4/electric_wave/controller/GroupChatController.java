package com.marm4.electric_wave.controller;

import com.marm4.electric_wave.Interface.OnGroupChatCompleteListener;
import com.marm4.electric_wave.model.GroupChat;
import com.marm4.electric_wave.model.User;
import com.marm4.electric_wave.service.GroupChatService;

import java.util.List;

public class GroupChatController {
    private GroupChatService groupChatService;
    public GroupChatController(){ this.groupChatService = new GroupChatService();}

    public void saveGroupChat(GroupChat chat){ groupChatService.saveGroupChat(chat); }

    private void getGroupChatById(String id, OnGroupChatCompleteListener listener, boolean readyReturn){
        groupChatService.getGroupChatById(id, listener, readyReturn);
    }

    public void getAllGroupChat(List<User> usersList, OnGroupChatCompleteListener listener){
        boolean readyReturn = false;
        int i=0;
        while (i < usersList.size()){
            i++;
            if(usersList.size() == i)
                readyReturn = true;

            getGroupChatById(usersList.get(i-1).getId(), listener, readyReturn);
        }
    }
}
