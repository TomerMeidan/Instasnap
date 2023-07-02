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

public class LoginView extends AppCompatActivity {

    private EditText _editTextEmail;
    private EditText _editTextPassword;
    private Button _loginButton;
    private FirebaseAuth _mAuth;
    private ProgressBar _loginProgressBar;
    private TextView _registerHereTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();

        setRegisterNowButtonListener();

        setLoginButtonListener();
    }

    private void initialize() {
        // Initializing registration objects
        _mAuth = FirebaseAuth.getInstance();
        _editTextEmail = findViewById(R.id.login_email_text_input);
        _editTextPassword = findViewById(R.id.login_password_text_input);
        _loginButton = findViewById(R.id.login_button);
        _loginProgressBar = findViewById(R.id.login_progressBar);
        _registerHereTextView = findViewById(R.id.register_now);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = _mAuth.getCurrentUser();
        if(currentUser != null){
            Intent homepageIntent = new Intent(getApplicationContext(), HomePageView.class);
            startActivity(homepageIntent);
            finish();
        }
    }

    private void setLoginButtonListener() {
        // Setting listener for login button
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _loginProgressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(_editTextEmail.getText());
                password = String.valueOf(_editTextPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginView.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginView.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                _mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                _loginProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(LoginView.this, "Login Succeed",
                                            Toast.LENGTH_SHORT).show();
                                    Intent homepageIntent = new Intent(getApplicationContext(), HomePageView.class);
                                    startActivity(homepageIntent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginView.this, "Login failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void setRegisterNowButtonListener() {
        // Setting listener for register now button
        _registerHereTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterView.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }
}