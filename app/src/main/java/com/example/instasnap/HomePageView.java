package com.example.instasnap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageView extends AppCompatActivity {

    private FirebaseAuth _auth;
    private Button _logoutButton;
    private TextView _welcomeText;
    private FirebaseUser _user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        initialize();

        checkLoginStatus();

        setLogoutButtonListener();

    }


    private void initialize() {
        _auth = FirebaseAuth.getInstance();
        _logoutButton = findViewById(R.id.logout_button);
        _welcomeText = findViewById(R.id.welcome_textview);
        _user = _auth.getCurrentUser();
    }

    private void setLogoutButtonListener() {
        _logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(getApplicationContext(), LoginView.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void checkLoginStatus() {
        _welcomeText.setText("Hello " + _user.getEmail());
    }

}