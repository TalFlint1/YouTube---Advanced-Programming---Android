package com.example.youtube_android;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private String title;
    private String owner;
    private String views;
    private String time_publish;
    private String thumbnailUrl;
    private String videoUrl;
    private int likeCount;
    private int id;
    private boolean liked;
    private List<Comment> comments;

    public Video(String title, String owner, String views, String time, String videoUrl, int likeCount) {
        this.title = title;
        this.owner = owner;
        this.views = views;
        this.liked =false;
        this.time_publish = time;
        this.thumbnailUrl = videoUrl;
        this.videoUrl = videoUrl;
        this.likeCount = likeCount;
        this.comments = new  ArrayList<>();
    }

    public Video(String title, String owner, String views, String time, String videoUrl, int likeCount, int id) {
        this.title = title;
        this.owner = owner;
        this.views = views;
        this.time_publish = time;
        this.thumbnailUrl = videoUrl;
        this.videoUrl = videoUrl;
        this.id = id;
        this.comments = new  ArrayList<>();
        this.liked =false;
    }

    public String getTitle() {
        return title;
    }
    public int getId() {
        return id;
    }

    public String getUsername() {
        return owner;
    }
    public void addComment(Comment c) {
        this.comments.add(c);
    }
    public void updateComment(int position ,String c) {
        this.comments.get(position).setMessage(c);
    }
    public void removeComment(int index) {
        this.comments.remove(index);
    }
    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    public void setLikes(int likes) {
        this.likeCount = likes;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getViews() {
        return views;
    }

    public String getTime() {
        return time_publish;
    }
    public  List<Comment> getComments() {
        return comments;
    }
    public  void setComments( List<Comment> comments) {
        this.comments = comments;
    }

    public String getVideoUrl() {
        return thumbnailUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }
}
