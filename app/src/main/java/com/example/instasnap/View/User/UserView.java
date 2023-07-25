package com.example.instasnap.View.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.instasnap.Model.Post;
import com.example.instasnap.Model.Story;
import com.example.instasnap.Model.User;
import com.example.instasnap.R;
import com.example.instasnap.Utils.Parser;
import com.example.instasnap.View.User.Adapters.PostRecyclerViewAdapter;
import com.example.instasnap.View.User.Adapters.StoryRecyclerViewAdapter;
import com.example.instasnap.View.User.Fragments.HomePageFragmentView;
import com.example.instasnap.View.User.Fragments.MainPageFragmentView;
import com.example.instasnap.View.User.Fragments.ScreenModeDialogView;
import com.example.instasnap.View.LoginView;
import com.example.instasnap.ViewModel.HomePageViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        initiateMainPageView();

    }

    private void initiateMainPageView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.user_container_view, MainPageFragmentView.class, null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();

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
                ScreenModeDialogView screenModeView = ScreenModeDialogView.newInstance("Theme Choice");
                screenModeView.show(fm, "screen_mode_fragment");
                return true;
            case R.id.menu_settings:
                // TODO Create fragment for settings menu, settings will consist of profile image edit for now
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getSavedTheme() {
        // Retrieve the theme from shared preference
        int defaultTheme = com.google.android.material.R.style.Base_V24_Theme_Material3_Light; // Default theme if no theme is found
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE);
        int currentTheme = sharedPreferences.getInt("current_theme", defaultTheme);
        setTheme(currentTheme);
    }


}