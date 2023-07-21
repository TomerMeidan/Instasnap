package com.example.instasnap.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.instasnap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageView extends AppCompatActivity {

    private FirebaseAuth _auth;
    private Button _logoutButton;
    private TextView _welcomeText;
    private FirebaseUser _user;
    private Button _screenMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        initialize();

        checkLoginStatus();

        // TODO Remove set logout listener, option menu will have this action
        setLogoutButtonListener();
        // TODO Remove set screen mode listener, option menu will have this action
        setScreenModeButtonListener();

    }

    private void initialize() {
        _auth = FirebaseAuth.getInstance();
        _logoutButton = findViewById(R.id.logout_button);
        _welcomeText = findViewById(R.id.welcome_textview);
        _user = _auth.getCurrentUser();
        _screenMode = findViewById(R.id.screen_mode_button);
    }

    private void checkLoginStatus() {
        _welcomeText.setText("Hello " + _user.getEmail());
    }

    // TODO Remove screen mode listener, option menu will have this action
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

    // TODO Remove logout listener, option menu will have this action
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(getApplicationContext(), LoginView.class);
                startActivity(loginIntent);
                finish();
                return true;
            case R.id.menu_screen_mode:
                FragmentManager fm = getSupportFragmentManager();
                ScreenModeDialog screenModeDialog = ScreenModeDialog.newInstance("Theme Choice");
                screenModeDialog.show(fm, "screen_mode_fragment");
                return true;
            case R.id.menu_settings:
                // TODO Create fragment for settings menu, settings will consist of profile image edit for now
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}