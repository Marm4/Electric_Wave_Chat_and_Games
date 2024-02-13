package com.marm4.electric_wave.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.marm4.electric_wave.Interface.OnLoadCurrentUserCompleteListener;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.FriendController;
import com.marm4.electric_wave.ui.auth.LogInActivity;
import com.marm4.electric_wave.controller.AuthController;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView topNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAuth();
        initUI();
    }

    private void checkAuth() {
        AuthController authController = new AuthController();

        if(!authController.isUserLoggedIn()){
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        }
        else {
            authController.loadCurrentUser(new OnLoadCurrentUserCompleteListener() {
                @Override
                public void onLoadCurrentUserComplete(Boolean load) {
                    FriendController friendController = new FriendController();
                    friendController.getAllFriendRequests();
                }
            });
        }
    }

    private void initUI() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        topNavView = findViewById(R.id.topNavView);
        NavigationUI.setupWithNavController(topNavView, navController);
    }

}