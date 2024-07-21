package com.example.youtube_android;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;

public class LoginViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponse;
    }

    // Perform login action
    // Perform login action
    public void login(LoginRequest loginRequest) {
        userRepository.loginUser(loginRequest, new UserRepository.LoginCallback() {
            @Override
            public void onLoginResponse(LoginResponse response) {
                loginResponse.setValue(response);
            }

            @Override
            public void onLoginError(String errorMessage) {
                loginResponse.setValue(null); // or handle error state as needed
            }
        });
    }
}
