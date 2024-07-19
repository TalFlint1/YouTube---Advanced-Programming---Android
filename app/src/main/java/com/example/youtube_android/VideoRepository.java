package com.example.youtube_android;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoRepository {
    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    public VideoRepository() {
        apiService = RetrofitClient.getApiService();
//        sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    // Method to perform login operation
    public void getAllVideos(final GetVideosCallback callback) {
        apiService.getVideos().enqueue(new Callback<List<Video>>() {


            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Save user data to SharedPreferences
                    // Log the SharedPreferences values
                   // displaySharedPreferences();
                    // Pass the login response to ViewModel
                    callback.onGetVideosResponse(response.body());
                } else if (response.code() == 401) {
                    String errorMessage = "Unauthorized access. Please check your credentials.";
                    Log.e("UserRepository", errorMessage);
                    callback.onGetVideosError(errorMessage);
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to login: " + response.message();
                    Log.e("UserRepository", errorMessage);
                    callback.onGetVideosError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                String errorMessage = "Network error. Please try again later.";
                Log.e("UserRepository", errorMessage, t);
                callback.onGetVideosError(errorMessage);
            }


        });
    }

    // Method to perform register operation
    public void UpdateVideo(Video video, final UpdateVideosCallback callback) {
        Log.e("here in update func",".");
        Log.e("here in update func",video.getUsername());
        Log.e("here in update func", String.valueOf(video.getId()));
        apiService.updateVideo(video.getUsername(), String.valueOf(video.getId()),video).enqueue(new Callback<Video>()
        {

            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("here in onResponse",".");
                    callback.onUpdateVideosResponse(response.body());
                } else if (response.code() == 401) {
                    String errorMessage = "Unauthorized access. Please check your credentials.";
                    Log.e("UserRepository", errorMessage);
                    callback.onUpdateVideosError(errorMessage);
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to login: " + response.message();
                    Log.e("UserRepository", errorMessage);
                    callback.onUpdateVideosError(errorMessage);
                }

            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.e("here in onFailure",".");

                String errorMessage = "Network error. Please try again later.";
                Log.e("UserRepository", errorMessage, t);
                callback.onUpdateVideosError(errorMessage);
            }
        });
    }

    public void GetVideo(Video video, final GetVideoCallback callback) {
        Log.e("here in update func",".");
        Log.e("here in update func",video.getUsername());
        Log.e("here in update func", String.valueOf(video.getId()));
        apiService.getVideo(video.getUsername(), String.valueOf(video.getId())).enqueue(new Callback<Video>()
        {

            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("here in onResponse",".");
                    callback.onGetVideoResponse(response.body());
                } else if (response.code() == 401) {
                    String errorMessage = "Unauthorized access. Please check your credentials.";
                    Log.e("UserRepository", errorMessage);
                    callback.onGetVideoError(errorMessage);
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to login1: " + response.message();
                    Log.e("UserRepository", errorMessage);
                    callback.onGetVideoError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.e("here in onFailure",".");

                String errorMessage = "Network error. Please try again later.";
                Log.e("UserRepository", errorMessage, t);
                callback.onGetVideoError(errorMessage);
            }
        });
    }


    public void CreateVideo(Video video, final CreateVideosCallback callback) {
        Log.e("here in create func",".");
        Log.e("here in create func",video.getUsername());
        Log.e("here in create func", String.valueOf(video.getId()));
        apiService.createVideo(video.getUsername(), video).enqueue(new Callback<Video>()
        {

            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("here in onResponse",".");
                    callback.onCreateVideosResponse(response.body());
                } else if (response.code() == 401) {
                    String errorMessage = "Unauthorized access. Please check your credentials.";
                    Log.e("UserRepository", errorMessage);
                    callback.onCreateVideosError(errorMessage);
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to login1: " + response.message();
                    Log.e("UserRepository", errorMessage);
                    callback.onCreateVideosError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.e("here in onFailure",".");

                String errorMessage = "Network error. Please try again later.";
                Log.e("UserRepository", errorMessage, t);
                callback.onCreateVideosError(errorMessage);
            }
        });
    }



    // Method to clear user data from SharedPreferences
    private void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Method to save token to SharedPreferences
    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwtToken", token);
        editor.apply();
    }

    // Method to save profile picture URL to SharedPreferences
    public void saveProfilePicture(String profilePictureUrl) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profilePictureUrl", profilePictureUrl);
        editor.apply();
    }

    // Method to display SharedPreferences values for debugging
    private void displaySharedPreferences() {
        Log.d("SharedPreferences", "Token: " + sharedPreferences.getString("jwtToken", ""));
        Log.d("SharedPreferences", "Profile Picture URL: " + sharedPreferences.getString("profilePictureUrl", ""));
    }

    // Interface to handle login callback
    public interface GetVideosCallback {
        void onGetVideosResponse(List<Video> response);
        void onGetVideosError(String errorMessage);
    }

    public interface UpdateVideosCallback {
        void onUpdateVideosResponse(Video response);
        void onUpdateVideosError(String errorMessage);
    }
    public interface CreateVideosCallback {
        void onCreateVideosResponse(Video response);
        void onCreateVideosError(String errorMessage);
    }
    public interface GetVideoCallback {
        void onGetVideoResponse(Video response);
        void onGetVideoError(String errorMessage);
    }

    // Interface to handle register callback
    public interface RegisterCallback {
        void onRegisterResponse(RegisterResponse response);
        void onRegisterError(String errorMessage);
    }

    // Interface to handle delete user callback
    public interface DeleteUserCallback {
        void onDeleteUserResponse();
        void onDeleteUserError(String errorMessage);
    }
}
