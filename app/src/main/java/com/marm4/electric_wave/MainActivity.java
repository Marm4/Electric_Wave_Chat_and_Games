package com.marm4.electric_wave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.marm4.electric_wave.auth.LogInActivity;
import com.marm4.electric_wave.controller.AuthController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAuth();
    }

    private void checkAuth() {
        AuthController authManager = new AuthController();
        if(!authManager.isUserLoggedIn()){
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        }
    }
}