package com.example.instasnap.Model;

import java.util.ArrayList;

public class User {
    public String username;
    public String email;

    public String profilePictureURL;
    public String password;

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<Story> getStories() { return stories; }

    public ArrayList<Post> posts;

    public ArrayList<Story> stories;
    private String uniqueID;

    public User(String email, String password, String profilePictureURL, ArrayList<Post> posts, ArrayList<Story> stories, String _uniqueID) {
        this.email = email;
        this.username = makeUserName(email);
        this.profilePictureURL = profilePictureURL;
        this.password = password;
        this.posts = posts;
        this.stories = stories;
        this.uniqueID = _uniqueID;
    }

    String makeUserName(String email){
        String[] split = email.split("@");
        return split[0];
    }

    public User(String email, String profilePictureId, String _uniqueID) {
        this.username = makeUserName(email);
        this.profilePictureURL = profilePictureId;
        this.uniqueID = _uniqueID;

    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void addStory(Story story){
        stories.add(story);
    }
}
