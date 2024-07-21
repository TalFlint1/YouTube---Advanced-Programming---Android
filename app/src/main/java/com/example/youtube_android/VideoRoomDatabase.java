package com.example.youtube_android;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {VideoEntity.class}, version = 1)
public abstract class VideoRoomDatabase extends RoomDatabase {
    public abstract VideoDao videoDao();

    private static volatile VideoRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static VideoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VideoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VideoRoomDatabase.class, "video_database.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
