package com.marm4.electric_wave.ui.auth;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.AuthController;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameET;
    private EditText userNameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText repeatPasswordET;
    private Button singUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        initializer();
        listener();
    }

    private void initializer() {
        nameET = findViewById(R.id.nameET);
        userNameET = findViewById(R.id.userNameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        repeatPasswordET = findViewById(R.id.repeatPasswordET);
        singUpBtn = findViewById(R.id.singUpBtn);


    }

    private void listener() {
        singUpBtn.setOnClickListener(v -> {
            String name = nameET.getText().toString();
            String userName = userNameET.getText().toString();
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            String repeatPassword = repeatPasswordET.getText().toString();

            if(!password.equals(repeatPassword) || password.isEmpty())
                Toast.makeText(SignUpActivity.this, "The passwords are different.", Toast.LENGTH_SHORT).show();

            else if(name.isEmpty() || userName.isEmpty() || email.isEmpty())
                Toast.makeText(SignUpActivity.this, "You need to fill out all the fields to register.", Toast.LENGTH_SHORT).show();

            else{
                Log.i("TAG", "Auth call");
                AuthController authController = new AuthController(getApplicationContext());
                authController.signUp(email, password, name, userName);
            }
        });
    }
}