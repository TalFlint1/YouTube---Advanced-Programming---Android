package com.example.youtube_android;

public class Video {
    private String title;
    private String username;
    private String views;
    private String time;
    private String videoUrl;
    private int likeCount;

    public Video(String title, String username, String views, String time, String videoUrl, int likeCount) {
        this.title = title;
        this.username = username;
        this.views = views;
        this.time = time;
        this.videoUrl = videoUrl;
        this.likeCount = likeCount;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getViews() {
        return views;
    }

    public String getTime() {
        return time;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }
}
