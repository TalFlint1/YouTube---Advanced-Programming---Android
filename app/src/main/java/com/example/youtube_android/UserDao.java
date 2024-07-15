package com.example.youtube_android;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    UserEntity getUserByUsernameAndPassword(String username, String password);

    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    LiveData<UserEntity> getUser(String username);

    @Query("DELETE FROM user_table WHERE username = :username")
    void deleteByUsername(String username); // Modify the method to delete by username

    @Query("DELETE FROM user_table")
    void deleteAll();
    @Query("SELECT * FROM user_table")
    LiveData<List<UserEntity>> getAllUsers();
}
