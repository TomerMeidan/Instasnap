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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.instasnap.Model.Post;
import com.example.instasnap.Model.Story;
import com.example.instasnap.Model.User;
import com.example.instasnap.R;
import com.example.instasnap.Utils.Parser;
import com.example.instasnap.View.LoginView;
import com.example.instasnap.View.ScreenModeDialog;
import com.example.instasnap.ViewModel.HomePageViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomePageView extends AppCompatActivity {

    private HomePageViewModel _homePageViewModel;
    private FirebaseAuth _auth;
    private FirebaseUser _firebaseUser;
    private ArrayList<User> _users; // TODO load posts here
    private ArrayList<Post> _allPosts; // TODO load posts here
    private ArrayList<Story> _stories; // TODO load stories here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        initialize();

        initializeRecyclerViews();
    }

    private void initializeRecyclerViews(){

        RecyclerView storyRecyclerView = findViewById(R.id.homepage_story_recycler_view);
        RecyclerView postRecyclerView = findViewById(R.id.homepage_post_recycler_view);

        _homePageViewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
        _homePageViewModel.setPostList(_allPosts);
        _homePageViewModel.setStoryList(_stories);

        // Create adapter passing in the sample user data
        StoryRecyclerViewAdapter storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
        PostRecyclerViewAdapter postRecyclerViewAdapter = new PostRecyclerViewAdapter(_users, _homePageViewModel);

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
        _firebaseUser = _auth.getCurrentUser();
        _users = Parser.parseUsers(this.getApplicationContext());
        _allPosts = Parser.getAllPosts(_users);
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