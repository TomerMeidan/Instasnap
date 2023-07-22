package com.example.instasnap.View.HomePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.instasnap.R;
import com.example.instasnap.View.LoginView;
import com.example.instasnap.View.ScreenModeDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageView extends AppCompatActivity {

    private FirebaseAuth _auth;
    private Button _logoutButton;
    private FirebaseUser _user;
    private Button _screenMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        initialize();

        setLogoutButtonListener();

        setScreenModeButtonListener();

    }

    private void initialize() {
        _auth = FirebaseAuth.getInstance();
        _logoutButton = findViewById(R.id.logout_button);
        _user = _auth.getCurrentUser();
        _screenMode = findViewById(R.id.screen_mode_button);
    }


    private void setScreenModeButtonListener() {
        _screenMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ScreenModeDialog screenModeDialog = ScreenModeDialog.newInstance("Theme Choice");
                screenModeDialog.show(fm, "screen_mode_fragment");
            }
        });
    }

    private void getSavedTheme() {
        // Retrieve the theme from shared preference
        int defaultTheme = com.google.android.material.R.style.Base_V24_Theme_Material3_Light; // Default theme if no theme is found
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);
        int currentTheme = sharedPreferences.getInt("current_theme", defaultTheme);
        setTheme(currentTheme);
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
}