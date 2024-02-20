package com.marm4.electric_wave.service;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marm4.electric_wave.Interface.OnGroupChatCompleteListener;
import com.marm4.electric_wave.global.CurrentAdapters;
import com.marm4.electric_wave.model.GroupChat;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.Message;
import com.marm4.electric_wave.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("RestrictedApi")
public class GroupChatService {

    private FirebaseDatabase database;

    public GroupChatService() {
        database = FirebaseDatabase.getInstance();
    }

    public void saveGroupChat(GroupChat chatGroup) {
        String userId = CurrentUser.getInstance().getUser().getId();
        String friendId = "";
        List<User> userList = chatGroup.getMembers();
        for(User user: userList){
            if(user!=null)
                if(!user.getId().equals(userId))
                    friendId = user.getId();
        }
        boolean onSecondCall = false;
        Log.i("GroupChatService", "Saving group chat for...");

        saveGroupChatForUser(chatGroup, userId, friendId, onSecondCall);
        saveGroupChatForUser(chatGroup, friendId, userId, true);
    }

    private void saveGroupChatForUser(GroupChat chatGroup, String userId, String friendId, boolean onSecondCall){
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("chat-group").child(friendId).child("chats");

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Message> messages = new HashMap<>();
                Log.i("GroupChatService", "Saving Chat for: " + userId);
                for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                    Message message = messagesSnapshot.getValue(Message.class);
                    if (message != null) {
                        messages.put(message.getId(), message);
                    }
                }

                for (Message message : chatGroup.getMessages()) {
                    if (message.getId().equals("null")) {
                        DatabaseReference messageRef = chatRef.push();
                        String messageId = messageRef.getKey();
                        if(onSecondCall)
                            message.setId(messageId);
                        messageRef.setValue(message);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("GroupChatService", "Error: " + databaseError.getMessage());
            }
        });
    }

    public void getGroupChatById(String friendId, OnGroupChatCompleteListener listener, boolean readyReturn) {
        String userId = CurrentUser.getInstance().getUser().getId();
        GroupChat chatGroup = new GroupChat();
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("chat-group").child(friendId).child("chats");

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatGroup.setId(friendId);

                chatGroup.addMembers(CurrentUser.getInstance().getUser());
                User user = CurrentUser.getInstance().getFriendById(friendId);
                if (user != null)
                    chatGroup.addMembers(user);

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    chatGroup.addMessages(message);
                    String messageId = messageSnapshot.getKey();
                    if (message != null)
                        message.setId(messageId);

                }
                Log.i("GroupChatService", "Saving group chat");
                if (!CurrentUser.getInstance().containsChatGroupList(chatGroup))
                    CurrentUser.getInstance().addChatGroupList(chatGroup);
                if (readyReturn)
                    listener.onGroupChatComplete();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("GroupChatService", "Error: " + databaseError.getMessage());
            }
        });

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    if(CurrentAdapters.getInstance().getChatAdapter()!=null){
                        CurrentAdapters.getInstance().notifyChangeChat(chatGroup.getMessages());
                    }
                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }
}
