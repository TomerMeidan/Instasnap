package com.example.instasnap.Model;

import java.util.ArrayList;
import java.util.Collection;

public class User {
    public String username;
    public String email;

    public String profilePictureId;
    public String password;

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<Story> getStories() { return stories; }

    public ArrayList<Post> posts;

    public ArrayList<Story> stories;

    public User(String email, String password, String profilePictureId, ArrayList<Post> posts, ArrayList<Story> stories) {
        this.email = email;
        this.username = makeUserName(email);
        this.profilePictureId = profilePictureId;
        this.password = password;
        this.posts = posts;
        this.stories = stories;
    }

    String makeUserName(String email){
        String[] split = email.split("@");
        return split[0];
    }

    public User(String email, String profilePictureId) {
        this.username = makeUserName(email);
        this.profilePictureId = profilePictureId;
    }
}
