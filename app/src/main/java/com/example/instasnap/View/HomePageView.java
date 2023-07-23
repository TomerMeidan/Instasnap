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
    private TextView _welcomeText;
    private FirebaseUser _user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        initialize();

        checkLoginStatus();



    }

    private void initialize() {
        _auth = FirebaseAuth.getInstance();
        _welcomeText = findViewById(R.id.welcome_textview);
        _user = _auth.getCurrentUser();

    }

    private void checkLoginStatus() {
        _welcomeText.setText("Hello " + _user.getEmail());
    }



    private void getSavedTheme() {
        // Retrieve the theme from shared preference
        int defaultTheme = com.google.android.material.R.style.Base_V24_Theme_Material3_Light; // Default theme if no theme is found
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);
        int currentTheme = sharedPreferences.getInt("current_theme", defaultTheme);
        setTheme(currentTheme);
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