package com.example.instasnap.Model;

public class Story {
    public final User user;
    public String storyPictureID;

    public Story(User user, String storyPictureID) {
        this.user = user;
        this.storyPictureID = storyPictureID;
    }
}
