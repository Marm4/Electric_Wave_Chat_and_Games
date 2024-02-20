package com.marm4.electric_wave.controller;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.marm4.electric_wave.Interface.OnLoadCurrentUserCompleteListener;
import com.marm4.electric_wave.Interface.OnProfilePictureCompleteListener;
import com.marm4.electric_wave.Interface.OnSearchUserCompleteListener;
import com.marm4.electric_wave.service.AuthService;

public class AuthController {
    private AuthService authService;

    public AuthController(Context context) {
        authService = new AuthService(context);
    }

    public AuthController(){
        authService = new AuthService();
    }

    public void logIn(String email, String password) {
        Log.i("AuthController", " --- LOGIN USER ---");
        authService.logIn(email, password);
    }

    public void signUp(String email, String password, String name, String userName) {
        Log.i("AuthController", " --- SIGN UP ---");
        authService.singUp(email, password, name, userName);
    }


    public boolean isUserLoggedIn() {

        Log.i("AuthController", " --- USER LOGGED ---");
        return authService.isUserLoggedIn();
    }

    public void searchUserByUserName(String searchTerm, OnSearchUserCompleteListener listener){
        Log.i("AuthController", " --- SEARCH BY USER NAME ---");
        authService.searchUserByUserName(searchTerm, listener);
    }

    public void loadCurrentUser(OnLoadCurrentUserCompleteListener listener){
        Log.i("AuthController", " --- LOAD CURRENT USER ---");
        authService.loadCurrentUser(listener);
    }

    public void changeProfilePicture(Uri uri, OnProfilePictureCompleteListener listener){
        Log.i("AuthController", " --- CHANGE PROFILE PICTURE ---");
        authService.changeProfilePicture(uri, listener);
    }

    public void setAuthStateListener(){
        authService.setAuthStateListener();
    }

}