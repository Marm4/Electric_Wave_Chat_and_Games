package com.marm4.electric_wave.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marm4.electric_wave.Interface.OnSearchUserCompleteListener;
import com.marm4.electric_wave.ui.MainActivity;
import com.marm4.electric_wave.ui.auth.LogInActivity;
import com.marm4.electric_wave.model.User;

import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private FirebaseAuth mAuth;
    private Context context;

    public AuthService(Context context) {

        mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public AuthService() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void logIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            redirectToActivity(MainActivity.class);
                        }
                    }
                });
    }

    public void singUp(String email, String password, String name, String userName){
        Log.i("TAG", "---Sing up---");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("TAG", "Sing up success");
                            saveUserData(name, userName, email);
                            redirectToActivity(MainActivity.class);

                        } else {
                            Log.i("TAG", "Sing up error");
                        }
                    }
                });
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        redirectToActivity(LogInActivity.class);
    }

    private void saveUserData(String name, String userName, String email){
        Log.i("TAG", "---Save user data---");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = databaseReference.child("users");

        String userId = usersRef.push().getKey();
        User user = new User(userId, name, userName, email);

        usersRef.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("TAG", "User successfully saved in the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("TAG", "Error saving user in database");
            }
        });
    }


    public void searchUser(String searchTerm, OnSearchUserCompleteListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = usersRef.orderByChild("userName").startAt(searchTerm).endAt(searchTerm + "\uf8ff");
        List<User> userList = new ArrayList<>();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            String userId = snapshot.getKey();
                            String name = user.getName();
                            String userName = user.getUserName();
                            String email = user.getEmail();
                            User foundUser = new User(userId, name, userName, email);
                            userList.add(foundUser);
                        }
                    }
                }
                listener.onSearchUserComplete(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onSearchUserError("Error: " + databaseError.getMessage());
            }
        });
    }


    public boolean isUserLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    private void redirectToActivity(Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
