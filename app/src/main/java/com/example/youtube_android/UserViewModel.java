package com.example.youtube_android;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<UserEntity> user;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        user = userRepository.getUserFromRoom();
    }

    public LiveData<UserEntity> getUser() {
        return user;
    }

    public void loginUser(LoginRequest loginRequest, UserRepository.LoginCallback callback) {
        userRepository.loginUser(loginRequest, callback);
    }

    public void registerUser(RegisterRequest registerRequest, UserRepository.RegisterCallback callback) {
        userRepository.registerUser(registerRequest, callback);
    }

    public void deleteUser(String username, UserRepository.DeleteUserCallback callback) {
        userRepository.deleteUser(username, callback);
    }
}