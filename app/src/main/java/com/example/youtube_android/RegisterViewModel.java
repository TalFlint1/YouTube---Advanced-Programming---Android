package com.example.youtube_android;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class RegisterViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MutableLiveData<RegisterResponse> registerResponse = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<RegisterResponse> getRegisterResponse() {
        return registerResponse;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Perform register action
    public void register(RegisterRequest registerRequest) {
        userRepository.registerUser(registerRequest, new UserRepository.RegisterCallback() {
            @Override
            public void onRegisterResponse(RegisterResponse response) {

                registerResponse.setValue(response);
            }

            @Override
            public void onRegisterError(String error) {
                registerResponse.setValue(null);
            }
        });
    }
}
