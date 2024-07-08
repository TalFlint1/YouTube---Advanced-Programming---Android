package com.example.youtube_android;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {
    @GET("/api/videos")
    Call<List<Video>> getVideos();

    @GET("/api/users/{username}")
    Call<User> getUserDetails(@Path("username") String username);

    @PATCH("/api/users/{id}")
    Call<User> updateUser(@Path("id") String userId, @Body User user);

    @DELETE("/api/users/{id}")
    Call<Void> deleteUser(@Path("id") String userId);

    @GET("/api/users/{id}/videos")
    Call<List<Video>> getUserVideos(@Path("id") String userId);

    @POST("/api/users/{id}/videos")
    Call<Video> createVideo(@Path("id") String userId, @Body Video video);

    @GET("/api/users/{id}/videos/{pid}")
    Call<Video> getVideo(@Path("id") String userId, @Path("pid") String videoId);

    @PATCH("/api/users/{id}/videos/{pid}")
    Call<Video> updateVideo(@Path("id") String userId, @Path("pid") String videoId, @Body Video video);

    @DELETE("/api/users/{id}/videos/{pid}")
    Call<Void> deleteVideo(@Path("id") String userId, @Path("pid") String videoId);

    @POST("/api/users")
    Call<User> createUser(@Body User user);

    @POST("/api/tokens")
    Call<JwtResponse> createToken(@Body LoginRequest loginRequest);

    // Example for POST /api/users/login
    @POST("/api/users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}

