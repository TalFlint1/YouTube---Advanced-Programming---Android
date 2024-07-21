package com.example.youtube_android;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VideoEntity video);

    @Query("SELECT * FROM video_table WHERE id = :id")
    LiveData<VideoEntity> getVideoById(String id);

    @Query("SELECT * FROM video_table")
    List<VideoEntity> getAllVideos();

    @Query("DELETE FROM video_table WHERE id = :id")
    void deleteById(String id);

    @Query("UPDATE video_table SET title = :title, username = :username, views = :views, time = :time, videoUrl = :videoUrl WHERE id = :id")
    void updateVideo(String id, String title, String username, int views, String time, String videoUrl);

    @Query("DELETE FROM video_table")
    void deleteAll();
}
