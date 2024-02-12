package com.marm4.electric_wave.auth;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marm4.electric_wave.MainActivity;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.model.User;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameET;
    private EditText userNameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText repeatPasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        initializer();
        singUp();
    }

    private void initializer() {
    }

    private void singUp() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String name = nameET.getText().toString();
        String userName = userNameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String repeatPassword = repeatPasswordET.getText().toString();

        while (!password.equals(repeatPassword)){
            Toast.makeText(SignUpActivity.this, "Log in fail.", Toast.LENGTH_SHORT).show();
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserData(name, userName);
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error  ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserData(String name, String userName){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = databaseReference.child("users");

        String userId = usersRef.push().getKey();
        User user = new User(userId, name, userName);

        usersRef.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "User successfully saved in the database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error saving user in database", e);
            }
        });
    }
}