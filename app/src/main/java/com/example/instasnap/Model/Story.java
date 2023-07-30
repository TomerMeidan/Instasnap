package com.example.instasnap.Model;

public class Story {
    private final String username;
    private final String profilePictureID;
    private final String userUniqueID;
    public String storyPictureID;

    public Story(String username, String profilePictureID, String userUniqueID, String storyPictureID) {
        this.username = username;
        this.profilePictureID = profilePictureID;
        this.userUniqueID = userUniqueID;
        this.storyPictureID = storyPictureID;
    }

    public String getUsername() {
        return username;
    }
    public String getProfilePictureID() {
        return profilePictureID;
    }

}
