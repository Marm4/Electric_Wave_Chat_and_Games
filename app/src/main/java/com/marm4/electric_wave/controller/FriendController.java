package com.marm4.electric_wave.controller;

import com.marm4.electric_wave.Interface.OnFriendRequestCompleteListener;
import com.marm4.electric_wave.Interface.OnSearchUserCompleteListener;
import com.marm4.electric_wave.service.FriendService;

public class FriendController {

    private FriendService friendService;

    public FriendController(){
        friendService = new FriendService();
    }

    public void sendFriendRequest(String id){
        friendService.sendFriendRequest(id);
    }

    public void requestExist(String id, OnFriendRequestCompleteListener listener){
        friendService.requestExist(id, listener);
    }

    public void getAllFriendRequests(){
        friendService.getAllFriendRequests();
    }

    public void acceptDeleteFriend(String friendId, Boolean accept){
        friendService.acceptDeleteFriend(friendId, accept);
    }
}
