package com.example.youtube_android;

import java.util.List;
import java.io.Serializable;

public class Comment implements Serializable {
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
    public void setMessage(String text) {
        this.text= text;
    }

    public int getUserImage() {
        return userImage;
    }
}
