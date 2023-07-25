package com.example.instasnap.Model;

import java.util.ArrayList;

public class User {
    public String username;
    public String profilePictureId;
    public String password;

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<Post> posts;

    public User(String username, String password, String profilePictureId, ArrayList<Post> posts) {
        this.username = username;
        this.profilePictureId = profilePictureId;
        this.password = password;
        this.posts = posts;
    }

    public User(String username, String profilePictureId) {
        this.username = username;
        this.profilePictureId = profilePictureId;
    }


}
