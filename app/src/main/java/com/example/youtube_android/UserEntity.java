package com.example.youtube_android;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class UserEntity {
    @PrimaryKey
    @NonNull
    private String username;
    private String password;
    private String token;
    private String profilePictureUrl;

    public UserEntity(@NonNull String username, String password, String token, String profilePictureUrl) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.profilePictureUrl = profilePictureUrl;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                '}';
    }
}
