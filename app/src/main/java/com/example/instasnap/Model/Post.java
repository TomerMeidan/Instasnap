package com.example.instasnap.Model;

public class Post {
    public final String postPictureId;
    public int likes;

    public User user;

    public Post( User user, String postPictureId, int likes) {
        this.user = user;
        this.postPictureId = postPictureId;
        this.likes = likes;
    }
}
