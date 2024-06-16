package com.example.youtube_android;

public class Comment {
    private String message;
    private int userImage;

    public Comment(String message, int userImage) {
        this.message = message;
        this.userImage = userImage;
    }

    public String getMessage() {
        return message;
    }

    public int getUserImage() {
        return userImage;
    }
}
