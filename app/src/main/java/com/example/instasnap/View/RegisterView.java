package com.example.instasnap.View;

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

import com.example.instasnap.Model.Post;
import com.example.instasnap.Model.Story;
import com.example.instasnap.Model.User;
import com.example.instasnap.R;
import com.example.instasnap.Utils.FirebaseHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.ArrayList;

public class RegisterView extends AppCompatActivity {

    private EditText _editTextEmail;
    private EditText _editTextPassword;
    private Button _registerButton;
    private FirebaseAuth _mAuth;
    private ProgressBar _registerProgressBar;
    private TextView _loginHereTextView;

    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);

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
    public void onBackPressed() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginView.class);
        startActivity(loginIntent);
        finish();
        getSupportFragmentManager().popBackStack();
    }

    private void setLoginNowButtonListener() {
        // Anonymous Variable Example: One of the two listener type implementations requested
        _loginHereTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginView.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    public void onAuthenticationComplete(Task<AuthResult> task) {
        _registerProgressBar.setVisibility(View.GONE);

        if (task.isSuccessful()) {
            Toast.makeText(RegisterView.this, "Account created", Toast.LENGTH_SHORT).show();
            return;
        }

        Exception exception = task.getException();
        String errorMessage;

        if (exception instanceof FirebaseAuthException) {
            errorMessage = ((FirebaseAuthException) exception).getErrorCode();
        } else {
            errorMessage = "Registration failed";
        }
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
    }



    private void setRegisterButtonListener() {
        // Setting listener variable for the register button
        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _registerProgressBar.setVisibility(View.VISIBLE);
                email = String.valueOf(_editTextEmail.getText());
                password = String.valueOf(_editTextPassword.getText());

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterView.this, "Email or password is empty", Toast.LENGTH_SHORT).show();
                    _registerProgressBar.setVisibility(View.GONE);
                    return;
                }

                // Authentication with firebase, creating and storing a new user on the cloud
                AuthCompleteListener authCompleteListener = new AuthCompleteListener(RegisterView.this);
                _mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(authCompleteListener);
            }
        });
    }

    // Member Class Example: One of the two listener type implementations requested
    private class AuthCompleteListener implements OnCompleteListener<AuthResult> {
        private final RegisterView _registerActivity;

        public AuthCompleteListener(RegisterView activity) {
            this._registerActivity = activity;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            _registerActivity.onAuthenticationComplete(task);
            // Add the new registered user to the Firestore Cloud
            User user = new User(email, password, "profile_pic_example", new ArrayList<Post>(),  new ArrayList<Story>(), _mAuth.getCurrentUser().getUid());
            user.addStory(new Story(user.getUsername(), user.profilePictureURL, user.getUniqueID(), null));
            FirebaseHandler firebaseHandler = new FirebaseHandler();
            firebaseHandler.setUserInFirestoreDataBase(user);
        }
    }
}