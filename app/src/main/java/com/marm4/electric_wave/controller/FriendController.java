package com.marm4.electric_wave.controller;

import android.util.Log;

import com.marm4.electric_wave.Interface.OnFriendRequestCompleteListener;
import com.marm4.electric_wave.service.FriendService;

public class FriendController {

    private FriendService friendService;

    public FriendController(){
        friendService = new FriendService();
    }

    public void sendFriendRequest(String id){
        Log.i("FriendController", " --- SEND FRIEND REQUEST ---");
        friendService.sendFriendRequest(id);
    }

    public void requestExist(String id, OnFriendRequestCompleteListener listener){
        Log.i("FriendController", " --- REQUEST EXIST? ---");
        friendService.FriendRequestExist(id, listener);
    }

    public void getAllFriendRequests(OnFriendRequestCompleteListener listener){
        Log.i("FriendController", " --- GET ALL FRIENDS REQUEST ---");
        friendService.getAllFriendRequests(listener);
    }

    public void acceptDeleteFriend(String friendId, Boolean accept){
        Log.i("FriendController", " --- ACCEPT / DELETE FRIEND---");
        friendService.acceptDeleteFriend(friendId, accept);
    }

    public void getFriendList(OnFriendRequestCompleteListener listener){
        Log.i("FriendController", " --- GET ALL FRIENDS---");
        friendService.getFriendList(listener);
    }
}
