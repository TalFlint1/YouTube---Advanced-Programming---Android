package com.example.youtube_android;

public class VideoItem {
    private String title;
    private String username;
    private String views;
    private String time;
    private String videoUrl;

    public VideoItem(String title, String username, String views, String time, String videoUrl) {
        this.title = title;
        this.username = username;
        this.views = views;
        this.time = time;
        this.videoUrl = videoUrl;
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
}
