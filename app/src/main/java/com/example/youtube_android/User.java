package com.example.youtube_android;

public class User {
    private String username;
    private String password;
    private String name;
    private String profile_picture;

    // Constructor
    public User(String username, String password, String name, String profile_picture) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.profile_picture = profile_picture;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }

    public String getProfilePicture() {
        return profile_picture;
    }
}
