package com.example.youtube_android;

import java.util.List;

public class Comment {
    private String text;
    private int likes;
    private int videoId;
    private List<Comment> replies;
    private int userImage;


    public Comment(String message, int userImage, int videoId) {
        this.text = message;
        this.likes = 0;
        this.userImage = userImage;
        this.videoId = videoId;
        this.replies=null;
    }

    public Comment(String message, int videoId) {
        this.text = message;
        this.likes = 0;
        this.userImage = 0;
        this.videoId = videoId;
        this.replies=null;
    }

    public String getMessage() {
        return text;
    }

    public int getUserImage() {
        return userImage;
    }
}
