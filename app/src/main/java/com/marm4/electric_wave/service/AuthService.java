package com.marm4.electric_wave.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marm4.electric_wave.Interface.OnLoadCurrentUserCompleteListener;
import com.marm4.electric_wave.Interface.OnProfilePictureCompleteListener;
import com.marm4.electric_wave.Interface.OnSearchUserCompleteListener;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.ui.main.MainActivity;
import com.marm4.electric_wave.ui.auth.LogInActivity;
import com.marm4.electric_wave.model.User;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private FirebaseAuth mAuth;
    private Context context;
    private FirebaseAuth.AuthStateListener mAuthListener;

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
                            Log.i("AuthService", "Login success");
                        }
                    }
                });
    }

    public void singUp(String email, String password, String name, String userName){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("AuthService", "Sing up success");
                            saveUserData(name.toLowerCase(), userName.toLowerCase(), email.toLowerCase());
                            redirectToActivity(MainActivity.class);

                        } else {
                            Log.i("AuthService", "Sing up error");
                        }
                    }
                });
    }

    private void saveUserData(String name, String userName, String email){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = databaseReference.child("users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userId = currentUser.getUid();
        User user = new User(userId, name, userName, email);

        usersRef.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("AuthService", "Save data success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("AuthService", "Save data error");
            }
        });
    }


    public void searchUserByUserName(String userName, OnSearchUserCompleteListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = usersRef.orderByChild("userName").startAt(userName).endAt(userName + "\uf8ff");
        List<User> userList = new ArrayList<>();
        Log.i("AuthService", "Searching by username");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            Log.i("AuthService", "User found, userName: " + userName + " id: " + user.getId());
                            userList.add(user);
                        }
                    }
                }
                listener.onSearchUserComplete(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("AuthService", "User not found error: " + databaseError.getMessage());
                listener.onSearchUserError("Error: " + databaseError.getMessage());
            }
        });
    }


    public boolean isUserLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("AuthService", "is user logged? " + (currentUser!=null));
        return currentUser != null;
    }

    public void loadCurrentUser(OnLoadCurrentUserCompleteListener listener){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("AuthService", "Loading current user...");

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        CurrentUser.getInstance().setUser(user);
                        Log.i("AuthService", "Curren user userName: " + user.getUserName());
                        listener.onLoadCurrentUserComplete(true);
                    } else {
                        listener.onLoadCurrentUserComplete(false);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("AuthService", "Loading current user error: " + databaseError.getMessage());
                }
            });
        }
    }

    private void redirectToActivity(Class<?> cls) {
        Log.i("AuthService", "redirecting to activity...");
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void changeProfilePicture(Uri uri, OnProfilePictureCompleteListener listener){
        Log.i("AuthService", "Changing profile picture...");
        String nameForProfilePicture = CurrentUser.getInstance().getUser().getId();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("profile_images/" + nameForProfilePicture);

        UploadTask uploadTask = storageRef.putFile(uri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri2 -> {
                String imageUrl = uri2.toString();
                Log.i("AuthService", "Profile picture url: " + imageUrl);
                setProfilePictureUrl(imageUrl, listener);
            });
        });
    }

    private void setProfilePictureUrl(String imageUrl, OnProfilePictureCompleteListener listener) {
        Log.i("AuthService", "Setting profile picture...");
        DatabaseReference profilePictureRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(CurrentUser.getInstance().getUser().getId())
                .child("profilePictureUrl");


        profilePictureRef.setValue(imageUrl).addOnSuccessListener(aVoid -> {
            CurrentUser.getInstance().getUser().setProfilePictureUrl(imageUrl);
            Log.i("AuthService", "Setting profile picture, url: " + imageUrl);
            DatabaseReference lastUpdateRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(CurrentUser.getInstance().getUser().getId())
                    .child("lastUpdate");

            long lastUpdate = System.currentTimeMillis();
            lastUpdateRef.setValue(lastUpdate).addOnSuccessListener(aVoid2 -> {
                Log.i("AuthService", "Setting last update: " + lastUpdate);
                CurrentUser.getInstance().getUser().setLastUpdate(lastUpdate);
                listener.onProfilePictureCompleteListener();
            });

        });


    }

    public void setAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    File directory = new File(context.getExternalFilesDir(null),("Electric-Wave-PP"));
                    if (directory.exists() && directory.isDirectory()) {
                        File[] files = directory.listFiles();
                        if (files != null)
                            for (File file : files)
                                file.delete();

                    }
                    if (mAuthListener != null)
                        mAuth.removeAuthStateListener(mAuthListener);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }


}
