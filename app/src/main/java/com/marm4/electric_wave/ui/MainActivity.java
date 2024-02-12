package com.marm4.electric_wave.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.ui.auth.LogInActivity;
import com.marm4.electric_wave.controller.AuthController;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView topNavView;
    private static final long BACK_PRESS_DELAY = 2000; // Tiempo en milisegundos
    private long backPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAuth();
        initUI();
    }

    private void checkAuth() {
        AuthController authManager = new AuthController();
        if(!authManager.isUserLoggedIn()){
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        }
    }

    private void initUI() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        topNavView = findViewById(R.id.topNavView);
        NavigationUI.setupWithNavController(topNavView, navController);
    }

}