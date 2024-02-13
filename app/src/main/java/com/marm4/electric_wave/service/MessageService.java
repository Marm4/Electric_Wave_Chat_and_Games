package com.marm4.electric_wave.service;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marm4.electric_wave.Interface.OnSearchMessageCompleteListener;
import com.marm4.electric_wave.model.Message;
import com.marm4.electric_wave.model.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("RestrictedApi")
public class MessageService {
    private FirebaseDatabase database;
    public MessageService(){
         database = FirebaseDatabase.getInstance();
    }

    public void saveMessage(Message message){
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("content", message.getContent());
        messageData.put("timestamp", message.getTimestamp());
        String senderId;

        if(message.isFriendMessage())
            senderId = message.getFriend().getId();
        else
            senderId = message.getCurrentUser().getId();

        messageData.put("senderId", senderId);
        String currentUserId = message.getCurrentUser().getId();
        User friend = message.getFriend();
        String friendId = friend.getId();

        DatabaseReference usersRef = database.getReference().child("users").child(currentUserId);


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    usersRef.child("messages").child(friendId).push().setValue(messageData)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully saved message"))
                            .addOnFailureListener(e -> Log.e(TAG, "Error saving message", e));
                } else {

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("name", friend.getName());
                    userData.put("username", friend.getUserName());
                    userData.put("email", friend.getEmail());

                    usersRef.child("messages").child(friendId).setValue(userData).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            usersRef.child("messages").child(friendId).push().setValue(messageData)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Mensaje guardado exitosamente"))
                                    .addOnFailureListener(e -> Log.e(TAG, "Error al guardar el mensaje", e));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error al verificar si el usuario existe", databaseError.toException());
            }
        });
    }

    public void retrieveMessages(String currentUserId, String friendId, OnSearchMessageCompleteListener listener) {
        DatabaseReference userMessagesRef = database.getReference().child("users").child(currentUserId).child("messages").child(friendId);

        userMessagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Message> messageList = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String content = messageSnapshot.child("content").getValue(String.class);
                    Date timestamp = new Date(messageSnapshot.child("timestamp").getValue(Long.class));
                    Boolean friendMessage = messageSnapshot.child("senderId").getValue(String.class).equals(currentUserId);

                    Message message  = new Message(friendId, null, null,  content, timestamp, friendMessage);
                    messageList.add(message);
                }

                listener.onSearchMessageComplete(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                listener.onSearchMessageError(databaseError.getMessage());
            }
        });
    }

}
