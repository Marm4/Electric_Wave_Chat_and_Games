package com.marm4.electric_wave.service;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marm4.electric_wave.Interface.OnFriendRequestCompleteListener;
import com.marm4.electric_wave.Interface.OnProfilePictureCompleteListener;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.User;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("RestrictedApi")
public class FriendService {

    private FirebaseDatabase database;


    public FriendService() {
        database = FirebaseDatabase.getInstance();
    }

    public void sendFriendRequest(String id) {
        DatabaseReference usersRef = database.getReference().child("users").child(id);
        Log.i("FriendService", "Sending friend request...");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> friendRequest = new HashMap<>();
                friendRequest.put("id", CurrentUser.getInstance().getUser().getId());

                DatabaseReference friendRequestRef = usersRef.child("friend-request").push();
                friendRequestRef.setValue(friendRequest)
                        .addOnSuccessListener(aVoid -> {
                            Log.i("FriendService", "Succes, friend request to: " + id);
                        })
                        .addOnFailureListener(e -> {
                            Log.i("FriendService", "Fail friend request");
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("FriendService", "Error on friend request: " + databaseError.getMessage());
            }
        });
    }

    public void FriendRequestExist(String id, OnFriendRequestCompleteListener listener){
        DatabaseReference usersRef = database.getReference().child("users").child(id);
        DatabaseReference friendRequestsRef = usersRef.child("friend-request");

        Query query = friendRequestsRef.orderByChild("id").equalTo(CurrentUser.getInstance().getUser().getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i("FriendService", "Friend request exist");
                    listener.onFriendRequestComplete(true);
                } else {
                    Log.i("FriendService", "Friend request no exist");
                    listener.onFriendRequestComplete(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("FriendService", "Error: " + error.getMessage());
            }

        });
    }

    public void getAllFriendRequests(OnFriendRequestCompleteListener listener) {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(CurrentUser.getInstance().getUser().getId()).child("friend-request");

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    String friendId = requestSnapshot.child("id").getValue(String.class);
                    User user = new User();
                    user.setId(friendId);
                    Log.i("FriendService", "Find friend request: " + friendId);
                    CurrentUser.getInstance().addRequestList(user);
                }
                loadFriendById(CurrentUser.getInstance().getRequestList(), listener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("FriendService", "Error: " + databaseError.getMessage());
            }
        });
    }

    public void acceptDeleteFriend(String friendId, Boolean accept) {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(CurrentUser.getInstance().getUser().getId()).child("friend-request");

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    String requestIdFriendId = requestSnapshot.child("id").getValue(String.class);
                    Log.i("FriendService", "Friend request accept or delete: ");
                    if (requestIdFriendId.equals(friendId)) {
                        if(accept){
                            Log.i("FriendService", "Accept, new friend: " + friendId);
                            DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference()
                                    .child("users").child(CurrentUser.getInstance().getUser().getId()).child("friends");
                            friendsRef.child(friendId).setValue(true);
                        }
                        else
                            Log.i("FriendService", "Delete, no new friend");
                        requestSnapshot.getRef().removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("FriendService", "Error: " + databaseError.getMessage());
            }
        });
    }

    public void getFriendList(OnFriendRequestCompleteListener listener){
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(CurrentUser.getInstance().getUser().getId())
                .child("friends");
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot requestSnapshot : snapshot.getChildren()){
                    User user = new User();
                    String userId = requestSnapshot.getKey();
                    user.setId(userId);
                    CurrentUser.getInstance().addFriendList(user);
                    Log.i("FriendService", "Go to loadFriendById...");
                }
                loadFriendById(CurrentUser.getInstance().getFriendList(), listener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void loadFriendById(List<User> requestList, OnFriendRequestCompleteListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        Query query = usersRef.orderByChild("id");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (User user : requestList) {
                    DataSnapshot userSnapshot = dataSnapshot.child(user.getId());
                    if (userSnapshot.exists()) {
                        User foundUser = userSnapshot.getValue(User.class);
                        if (foundUser != null) {
                            user.setName(foundUser.getName());
                            user.setUserName(foundUser.getUserName());
                            user.setEmail(foundUser.getEmail());
                            user.setProfilePictureUrl(foundUser.getProfilePictureUrl());
                            Log.i("FriendService", "Friend found, userName: " + user.getUserName() + " id: " + user.getId());
                        }
                    }
                }
                listener.onFriendRequestComplete(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


}

