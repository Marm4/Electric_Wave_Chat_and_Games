package com.marm4.electric_wave.controller;

import android.content.Context;

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
        authService.logIn(email, password);
    }

    public void signUp(String email, String password, String name, String userName) {
        authService.singUp(email, password, name, userName);
    }

    public void logOut() {
        authService.logOut();
    }

    public boolean isUserLoggedIn() {
        return authService.isUserLoggedIn();
    }

    public void searchUsers(String searchTerm, OnSearchUserCompleteListener listener){
        authService.searchUser(searchTerm, listener);
    }

    public void loadCurrentUser(){
        authService.loadCurrentUser();
    }
}