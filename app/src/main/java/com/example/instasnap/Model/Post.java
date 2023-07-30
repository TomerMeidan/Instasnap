package com.example.instasnap.Model;

public class Post {

    private final String _postPictureURL;

    public final String username;
    public final String profilePictureURL;
    private final String userUniqueID;
    public int likes;

    public Post(String username, String profilePictureURL, String userUniqueID, String postPictureURL, int likes) {
        this.username = username;
        this.profilePictureURL = profilePictureURL;
        this.userUniqueID = userUniqueID;
        this._postPictureURL = postPictureURL;
        this.likes = likes;
    }

    public String getPostPictureURL() {
        return _postPictureURL;
    }

    public int getLikes() {
        return likes;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public String getUserUniqueID() {
        return userUniqueID;
    }


}
