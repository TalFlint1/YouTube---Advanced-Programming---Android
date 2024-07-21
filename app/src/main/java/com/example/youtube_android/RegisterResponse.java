package com.example.youtube_android;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("profile_picture")
    private String profilePictureUrl;

    // Constructor, getters, and setters
    public RegisterResponse(String token, String profilePictureUrl) {
        this.token = token;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getToken() {
        return token;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
