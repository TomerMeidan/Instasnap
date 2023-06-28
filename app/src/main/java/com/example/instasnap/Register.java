package com.example.instasnap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private ProgressBar registerProgressBar;
    private TextView loginHereTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();

        setLoginNowButtonListener();

        setRegisterButtonListener();

    }

    private void initialize() {
        // Initializing registration objects
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.register_email_text_input);
        editTextPassword = findViewById(R.id.register_password_text_input);
        registerButton = findViewById(R.id.register_button);
        registerProgressBar = findViewById(R.id.register_progressBar);
        loginHereTextView = findViewById(R.id.login_now);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent homepageIntent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(homepageIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setLoginNowButtonListener() {
        // Setting listener for login now button
        loginHereTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void setRegisterButtonListener() {
        // Setting listener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerProgressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Authentication with firebase, creating and storing a new user on the cloud
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                registerProgressBar.setVisibility(View.GONE);

                                // TODO Add 'if' statements on why the Account wasn't created

                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Account wasn't created.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}