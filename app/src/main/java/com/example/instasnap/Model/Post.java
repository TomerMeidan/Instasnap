package com.example.instasnap.Model;

public class Post {
    public final User user;
    public final String postPictureId;
    public int likes;

    public Post(User user, String postPictureId, int likes) {
        this.user = user;
        this.postPictureId = postPictureId;
        this.likes = likes;
    }
}
