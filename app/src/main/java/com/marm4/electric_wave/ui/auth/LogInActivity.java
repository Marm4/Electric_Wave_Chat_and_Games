package com.marm4.electric_wave.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.AuthController;

public class LogInActivity extends AppCompatActivity {

    private EditText emailET;
    private EditText passwordET;
    private Button logInBtn;
    private Button singUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializer();
        listeners();
    }

    private void initializer() {
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        logInBtn = findViewById(R.id.loginBtn);
        singUpBtn = findViewById(R.id.singUpBtn);
    }

    private void listeners() {
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void logIn(){
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        AuthController authController = new AuthController(this);
        authController.logIn(email, password);
    }
}