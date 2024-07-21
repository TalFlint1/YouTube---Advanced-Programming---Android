package com.example.youtube_android;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class VideoRepository {
    private ApiService apiService;
    private VideoDao videoDao;
    private Context context;

    public VideoRepository(Context context) {
        apiService = RetrofitClient.getApiService();
        VideoRoomDatabase db = VideoRoomDatabase.getDatabase(context);
        videoDao = db.videoDao();
        this.context = context;
    }

    // Method to fetch all videos from API and store them in Room database
    public void getAllVideos(final GetVideosCallback callback) {
        apiService.getVideos().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Save videos to Room database
                    List<Video> videoList = response.body();
                    VideoRoomDatabase.databaseWriteExecutor.execute(() -> {
                        for (Video video : videoList) {
                            VideoEntity videoEntity = new VideoEntity();
                            videoEntity.setId(String.valueOf(video.getId()));
                            videoEntity.setTitle(video.getTitle());
                            videoEntity.setUsername(video.getUsername());
                            videoEntity.setViews(Integer.parseInt(video.getViews()));
                            videoEntity.setTime(video.getTime());
                            videoEntity.setVideoUrl(video.getVideoUrl());
                            videoDao.insert(videoEntity);
                        }
                        // Pass the video list response to ViewModel
                        callback.onGetVideosResponse(videoList);
                    });
                } else if (response.code() == 401) {
                    String errorMessage = "Unauthorized access. Please check your credentials.";
                    Log.e("VideoRepository", errorMessage);
                    callback.onGetVideosError(errorMessage);
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to fetch videos: " + response.message();
                    Log.e("VideoRepository", errorMessage);
                    callback.onGetVideosError(errorMessage);
                }
            }

            public List<Video> parseVideoEntitiesToVideos(List<VideoEntity> videoEntities) {
                List<Video> videos = new ArrayList<>();
                for (VideoEntity entity : videoEntities) {
                    Video video = new Video(
                            entity.getTitle(),
                            entity.getUsername(),
                            String.valueOf(entity.getViews()),
                            entity.getTime(),
                            entity.getVideoUrl(),
                            0, // Default like count, update if necessary
                            Integer.parseInt(entity.getId()) // Assuming ID is stored as a string in the entity
                    );
                    videos.add(video);
                }
                return videos;
            }
            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                String errorMessage = "Network error. Please try again later.";
                Log.e("VideoRepository", errorMessage, t);
                Log.e("VideoRepository", errorMessage, t);
                callback.onGetVideosError(errorMessage);
                UserRoomDatabase.databaseWriteExecutor.execute(() -> {
                    List<VideoEntity> videos = videoDao.getAllVideos();
                    if (videos != null) {
//                        // Offline login successful
//                        LoginResponse offlineLoginResponse = new LoginResponse(userEntity.getToken(), userEntity.getProfilePictureUrl());
                        runOnUiThread(() -> callback.onGetVideosResponse(parseVideoEntitiesToVideos(videos)));
                    }
                   else {
                        // Offline login failed
                       String msg = "Login failed. Please try again.";
                       runOnUiThread(() -> callback.onGetVideosError(errorMessage));
                    }
                });
            }
        });
    }



    // Method to update a video in API and Room
    public void updateVideo(Video video, final UpdateVideosCallback callback) {
        apiService.updateVideo(video.getUsername(), String.valueOf(video.getId()), video).enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Video updatedVideo = response.body();
                    VideoEntity videoEntity = new VideoEntity();
                    videoEntity.setId(String.valueOf(updatedVideo.getId()));
                    videoEntity.setTitle(updatedVideo.getTitle());
                    videoEntity.setUsername(updatedVideo.getUsername());
                    videoEntity.setViews(Integer.parseInt(video.getViews()));
                    videoEntity.setTime(updatedVideo.getTime());
                    videoEntity.setVideoUrl(updatedVideo.getVideoUrl());

                    VideoRoomDatabase.databaseWriteExecutor.execute(() -> {
                        videoDao.updateVideo(videoEntity.getId(),videoEntity.getTitle(),videoEntity.getUsername()
                        ,videoEntity.getViews(),videoEntity.getTime(),videoEntity.getVideoUrl());
                        callback.onUpdateVideosResponse(updatedVideo);
                    });
                } else if (response.code() == 401) {
                    String errorMessage = "Unauthorized access. Please check your credentials.";
                    Log.e("VideoRepository", errorMessage);
                    callback.onUpdateVideosError(errorMessage);
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to update video: " + response.message();
                    Log.e("VideoRepository", errorMessage);
                    callback.onUpdateVideosError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                String errorMessage = "Network error. Please try again later.";
                Log.e("VideoRepository", errorMessage, t);
                callback.onUpdateVideosError(errorMessage);
            }
        });
    }

    // Method to get a single video from API
    public void getVideo(Video video, final GetVideoCallback callback) {
        apiService.getVideo(video.getUsername(), String.valueOf(video.getId())).enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onGetVideoResponse(response.body());
                } else if (response.code() == 401) {
                    String errorMessage = "Unauthorized access. Please check your credentials.";
                    Log.e("VideoRepository", errorMessage);
                    callback.onGetVideoError(errorMessage);
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to fetch video: " + response.message();
                    Log.e("VideoRepository", errorMessage);
                    callback.onGetVideoError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                String errorMessage = "Network error. Please try again later.";
                Log.e("VideoRepository", errorMessage, t);
                callback.onGetVideoError(errorMessage);
            }
        });
    }

    public void createVideo(Video video, final CreateVideosCallback callback) {
        // First, add the video to the API
        apiService.createVideo(video.getUsername(), video).enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Video createdVideo = response.body();
                    VideoEntity videoEntity = new VideoEntity();
                    videoEntity.setId(String.valueOf(createdVideo.getId()));
                    videoEntity.setTitle(createdVideo.getTitle());
                    videoEntity.setUsername(createdVideo.getUsername());
                    videoEntity.setViews(Integer.parseInt(createdVideo.getViews()));
                    videoEntity.setTime(createdVideo.getTime());
                    videoEntity.setVideoUrl(createdVideo.getVideoUrl());

                    // Insert the video into Room database
                    VideoRoomDatabase.databaseWriteExecutor.execute(() -> {
                        videoDao.insert(videoEntity);

                        // Notify callback with the created video
                        runOnUiThread(() -> callback.onCreateVideosResponse(createdVideo));
                    });
                } else if (response.code() == 401) {
                    String errorMessage = "Unauthorized access. Please check your credentials.";
                    Log.e("VideoRepository", errorMessage);
                    runOnUiThread(() -> callback.onCreateVideosError(errorMessage));
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to create video: " + response.message();
                    Log.e("VideoRepository", errorMessage);
                    runOnUiThread(() -> callback.onCreateVideosError(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                String errorMessage = "Network error. Please try again later.";
                Log.e("VideoRepository", errorMessage, t);
                runOnUiThread(() -> callback.onCreateVideosError(errorMessage));
            }
        });
    }

    // Callback interfaces
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
    private void runOnUiThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
