package com.example.youtube_android;

import java.util.ArrayList;
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
        this.replies = new ArrayList<>();
    }

    public Comment(String message, int videoId) {
        this.text = message;
        this.likes = 0;
        this.userImage = 0;
        this.videoId = videoId;
        this.replies = new ArrayList<>();
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

    public List<Comment> getReplies() {
        return replies;
    }

    public void addReply(Comment reply) {
        replies.add(reply);
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
