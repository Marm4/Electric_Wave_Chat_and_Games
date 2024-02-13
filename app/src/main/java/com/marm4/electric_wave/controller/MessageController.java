package com.marm4.electric_wave.controller;

import com.marm4.electric_wave.Interface.OnSearchMessageCompleteListener;
import com.marm4.electric_wave.model.Message;
import com.marm4.electric_wave.service.MessageService;

public class MessageController {

    private MessageService messageService;

    public MessageController() {
        messageService = new MessageService();
    }

    public void saveMessage(Message message){
        messageService.saveMessage(message);
    }

    public void retrieveMessage(String currentUserId, String friendId, OnSearchMessageCompleteListener listener){
        messageService.retrieveMessages(currentUserId, friendId, listener);
    }
}
