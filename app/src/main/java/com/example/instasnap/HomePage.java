package com.example.instasnap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button logoutButton;
    private TextView welcomeText;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initialize();

        checkLoginStatus();

        setLogoutButtonListener();

    }


    private void initialize() {
        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout_button);
        welcomeText = findViewById(R.id.welcome_textview);
        user = auth.getCurrentUser();
    }

    private void setLogoutButtonListener() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void checkLoginStatus() {
        // Check if user is logged in
        if(user == null){
            Intent loginIntent = new Intent(getApplicationContext(), Login.class);
            startActivity(loginIntent);
            finish();
        } else {
            welcomeText.setText("Hello " + user.getEmail());
        }
    }

}