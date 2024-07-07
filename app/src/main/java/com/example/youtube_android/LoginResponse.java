package com.example.youtube_android;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("profile_picture")
    private String profilePictureUrl;

    // Constructor, getters, and setters
    public LoginResponse(String token, String profilePictureUrl) {
        this.token = token;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
