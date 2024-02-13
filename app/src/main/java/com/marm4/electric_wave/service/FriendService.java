package com.marm4.electric_wave.service;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marm4.electric_wave.Interface.OnFriendRequestCompleteListener;
import com.marm4.electric_wave.model.CurrentUser;
import com.marm4.electric_wave.model.Message;
import com.marm4.electric_wave.model.User;

import java.util.ArrayList;
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

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> friendRequest = new HashMap<>();
                friendRequest.put("id", CurrentUser.getInstance().getUser().getId());

                DatabaseReference friendRequestRef = usersRef.child("friend-request").push();
                friendRequestRef.setValue(friendRequest)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Friend request sent");
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Friend request non-sent", e);
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error ", databaseError.toException());
            }
        });
    }

    public void requestExist(String id, OnFriendRequestCompleteListener listener){
        DatabaseReference usersRef = database.getReference().child("users").child(id);
        DatabaseReference friendRequestsRef = usersRef.child("friend-request");

        Query query = friendRequestsRef.orderByChild("id").equalTo(CurrentUser.getInstance().getUser().getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onFriendRequestComplete(true);
                } else {
                    listener.onFriendRequestComplete(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFriendRequestError("Error");
            }
        });
    }

    public void getAllFriendRequests() {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(CurrentUser.getInstance().getUser().getId()).child("friend-request");

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    String requestId = requestSnapshot.getKey();
                    String friendId = requestSnapshot.child("id").getValue(String.class);
                    User user = new User();
                    user.setId(friendId);
                    CurrentUser.getInstance().getRequestList().add(user);
                }
                loadRequestById();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching friend requests", databaseError.toException());
            }
        });
    }

    private void loadRequestById() {
        List<User> requestList = CurrentUser.getInstance().getRequestList();
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
                            Log.i("Log", "Load request success");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de consulta
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

                    if (requestIdFriendId.equals(friendId)) {
                        if(accept){
                            DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference()
                                    .child("users").child(CurrentUser.getInstance().getUser().getId()).child("friends");
                            friendsRef.child(friendId).setValue(true);
                        }
                        requestSnapshot.getRef().removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error accepting friend request", databaseError.toException());
            }
        });
    }
}

