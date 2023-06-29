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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private EditText _editTextEmail;
    private EditText _editTextPassword;
    private Button _registerButton;
    private FirebaseAuth _mAuth;
    private ProgressBar _registerProgressBar;
    private TextView _loginHereTextView;


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
        _mAuth = FirebaseAuth.getInstance();
        _editTextEmail = findViewById(R.id.register_email_text_input);
        _editTextPassword = findViewById(R.id.register_password_text_input);
        _registerButton = findViewById(R.id.register_button);
        _registerProgressBar = findViewById(R.id.register_progressBar);
        _loginHereTextView = findViewById(R.id.login_now);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = _mAuth.getCurrentUser();
        if(currentUser != null){
            Intent homepageIntent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(homepageIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent loginIntent = new Intent(getApplicationContext(), Login.class);
        startActivity(loginIntent);
        finish();
    }

    private void setLoginNowButtonListener() {
        // Anonymous Variable Example: One of the two listener type implementations requested
        _loginHereTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }


    // Member Class Example: One of the two listener type implementations requested
    private class AuthCompleteListener implements OnCompleteListener<AuthResult> {
        private final Register _registerActivity;

        public AuthCompleteListener(Register activity) {
            this._registerActivity = activity;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            _registerActivity.onAuthenticationComplete(task);
        }
    }

    public void onAuthenticationComplete(Task<AuthResult> task) {
        _registerProgressBar.setVisibility(View.GONE);

        if (task.isSuccessful()) {
            Toast.makeText(Register.this, "Account created", Toast.LENGTH_SHORT).show();
        } else {
            // Registration failed, handle the error
            try {
                throw task.getException();
            } catch (FirebaseAuthWeakPasswordException e) {
                // Handle weak password error
                String errorMessage = "Weak password";
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            } catch (FirebaseAuthInvalidCredentialsException e) {
                // Handle invalid email error
                String errorMessage = "Invalid email address";
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            } catch (FirebaseAuthUserCollisionException e) {
                // Handle email already in use error
                String errorMessage = "Email already in use";
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // Handle other exceptions
                String errorMessage = "Registration failed";
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setRegisterButtonListener() {
        // Setting listener variable for the register button
        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password;

                _registerProgressBar.setVisibility(View.VISIBLE);
                email = String.valueOf(_editTextEmail.getText());
                password = String.valueOf(_editTextPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    _registerProgressBar.setVisibility(View.GONE);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    _registerProgressBar.setVisibility(View.GONE);
                    return;
                }

                // Authentication with firebase, creating and storing a new user on the cloud
                AuthCompleteListener authCompleteListener = new AuthCompleteListener(Register.this);
                _mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(authCompleteListener);

            }
        });
    }
}