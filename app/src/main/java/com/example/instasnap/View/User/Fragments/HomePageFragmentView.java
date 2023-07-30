package com.example.instasnap.View.User.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instasnap.Model.Post;
import com.example.instasnap.Model.Story;
import com.example.instasnap.Model.User;
import com.example.instasnap.R;
import com.example.instasnap.Utils.FirebaseHandler;
import com.example.instasnap.Utils.Parser;
import com.example.instasnap.View.User.Adapters.PostRecyclerViewAdapter;
import com.example.instasnap.View.User.Adapters.StoryRecyclerViewAdapter;
import com.example.instasnap.ViewModel.HomePageViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class HomePageFragmentView extends Fragment {

    private HomePageViewModel _homePageViewModel;
    private ArrayList<User> _users; // TODO load posts here
    private ArrayList<Post> _allPosts; // TODO load posts here
    private ArrayList<Story> _allStories; // TODO load stories here

    public HomePageFragmentView() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            initializeRecyclerViews(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page_fragment, container, false);
    }

    private void initializeRecyclerViews(View view){

        RecyclerView storyRecyclerView = view.findViewById(R.id.homepage_story_recycler_view);
        RecyclerView postRecyclerView = view.findViewById(R.id.homepage_post_recycler_view);

        _homePageViewModel = new ViewModelProvider(requireActivity()).get(HomePageViewModel.class);

        _homePageViewModel.setPostList(_allPosts);
        _homePageViewModel.setStoryList(_allStories);
        _homePageViewModel.setUserList(_users);

        // Create adapter passing in the sample user data
        StoryRecyclerViewAdapter storyRecyclerViewAdapter = new StoryRecyclerViewAdapter(_allStories, _homePageViewModel);
        PostRecyclerViewAdapter postRecyclerViewAdapter = new PostRecyclerViewAdapter(_allPosts, _homePageViewModel);

        RecyclerView.LayoutManager storyLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL,false);
        RecyclerView.LayoutManager postLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false);

        // Set layout manager to position the items
        storyRecyclerView.setLayoutManager(storyLayoutManager);
        postRecyclerView.setLayoutManager(postLayoutManager);

        // Attach the adapter to the recyclerview to populate items
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);
        postRecyclerView.setAdapter(postRecyclerViewAdapter);

        _homePageViewModel.getMutableUserPostList().observe(getActivity(), new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> posts) {
                postRecyclerViewAdapter.updatePostList(posts);
            }
        });

        // TODO In later stages HERE is a good place to observe to "viewing stories" events.
    }

    private void initialize() {
        FirebaseHandler firebaseHandler = new FirebaseHandler();
        _users = firebaseHandler.readDataFromFirebase();
        _allPosts = Parser.getAllPosts(_users);
        _allStories = Parser.getAllStories(_users);
    }

}