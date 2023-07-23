package com.example.instasnap.View.HomePage;

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
import android.view.View;
import android.widget.Button;

import com.example.instasnap.Model.Post;
import com.example.instasnap.Model.Story;
import com.example.instasnap.R;
import com.example.instasnap.View.LoginView;
import com.example.instasnap.View.ScreenModeDialog;
import com.example.instasnap.ViewModel.HomePageViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomePageView extends AppCompatActivity {

    private HomePageViewModel _homePageViewModel;
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

        initializeRecyclerViews();
    }

    private void initializeRecyclerViews(){

        ArrayList<Post> posts = new ArrayList<>(); // TODO load posts here
        ArrayList<Story> stories = new ArrayList<>(); // TODO load stories here

        RecyclerView storyRecyclerView = findViewById(R.id.homepage_story_recycler_view);
        RecyclerView postRecyclerView = findViewById(R.id.homepage_post_recycler_view);

        _homePageViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
        _homePageViewModel.setPostList(posts);
        _homePageViewModel.setStoryList(stories);

        // Create adapter passing in the sample user data
        StoryRecyclerViewAdapter storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
        PostRecyclerViewAdapter postRecyclerViewAdapter = new PostRecyclerViewAdapter(posts, _homePageViewModel);

        RecyclerView.LayoutManager storyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        RecyclerView.LayoutManager postLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);

        // Set layout manager to position the items
        storyRecyclerView.setLayoutManager(storyLayoutManager);
        postRecyclerView.setLayoutManager(postLayoutManager);

        // Attach the adapter to the recyclerview to populate items
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);
        postRecyclerView.setAdapter(postRecyclerViewAdapter);

        _homePageViewModel.getMutableUserPostList().observe(this, new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> posts) {
                postRecyclerViewAdapter.updatePostList(posts);
            }
        });

        // TODO In later stages HERE is a good place to observe to "viewing stories" events.
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