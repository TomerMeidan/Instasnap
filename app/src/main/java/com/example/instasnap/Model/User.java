package com.example.instasnap.Model;

import java.util.ArrayList;
import java.util.Collection;

public class User {
    public String username;
    public String profilePictureId;
    public String password;

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<Story> getStories() { return stories; }

    public ArrayList<Post> posts;

    public ArrayList<Story> stories;

    public User(String username, String password, String profilePictureId, ArrayList<Post> posts, ArrayList<Story> stories) {
        this.username = username;
        this.profilePictureId = profilePictureId;
        this.password = password;
        this.posts = posts;
        this.stories = stories;
    }

    public User(String username, String profilePictureId) {
        this.username = username;
        this.profilePictureId = profilePictureId;
    }
}
