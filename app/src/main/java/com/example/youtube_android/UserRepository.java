package com.example.youtube_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.List;
import androidx.lifecycle.LiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private ApiService apiService;
    private SharedPreferences sharedPreferences;
    private UserDao userDao;
    private LiveData<UserEntity> user;
    private Context context;

    public UserRepository(Context context) {
        apiService = RetrofitClient.getApiService();
        sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        UserRoomDatabase db = UserRoomDatabase.getDatabase(context);
        userDao = db.userDao();
        this.context = context;
    }

    // Method to perform login operation
    public void loginUser(LoginRequest loginRequest, final LoginCallback callback) {
        apiService.loginUser(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String profilePictureUrl = loginResponse.getProfilePictureUrl();
                    // Save user data to SharedPreferences
                    saveToken(loginResponse.getToken());
                    saveProfilePicture(profilePictureUrl);
                    // Log the SharedPreferences values
//                    displaySharedPreferences();

                    // Save to Room database
                    UserEntity userEntity = new UserEntity(loginRequest.getUsername(), loginRequest.getPassword(), loginResponse.getToken(), profilePictureUrl);
                    userEntity.setUsername(loginRequest.getUsername());
                    userEntity.setToken(loginResponse.getToken());
                    userEntity.setProfilePictureUrl(profilePictureUrl);
                    // Log user entity details
                    Log.d("UserRepository", "Saving user to Room database: " + userEntity.toString());
                    UserRoomDatabase.databaseWriteExecutor.execute(() -> {
                        userDao.insert(userEntity);
                        runOnUiThread(() -> callback.onLoginResponse(loginResponse));
                        Log.d("UserRepository", "User saved to Room database: " + userEntity.toString());
                    });

                    // Pass the login response to ViewModel
//                    callback.onLoginResponse(loginResponse);
                } else if (response.code() == 401) {
                    String errorMessage = "Unauthorized access. Please check your credentials.";
                    Log.e("UserRepository", errorMessage);
                    runOnUiThread(() -> callback.onLoginError(errorMessage));
//                    callback.onLoginError(errorMessage);
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to login: " + response.message();
                    Log.e("UserRepository", errorMessage);
//                    callback.onLoginError(errorMessage);
                    runOnUiThread(() -> callback.onLoginError(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("UserRepository", "Network error. Attempting offline login.", t);
                // Network error, attempt to login using Room database
                UserRoomDatabase.databaseWriteExecutor.execute(() -> {
                    UserEntity userEntity = userDao.getUserByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
                    if (userEntity != null) {
                        // Offline login successful
                        LoginResponse offlineLoginResponse = new LoginResponse(userEntity.getToken(), userEntity.getProfilePictureUrl());
//                        callback.onLoginResponse(offlineLoginResponse);
                        runOnUiThread(() -> callback.onLoginResponse(offlineLoginResponse));
                    } else {
                        // Offline login failed
                        String errorMessage = "Login failed. Please try again.";
//                        callback.onLoginError(errorMessage);
                        runOnUiThread(() -> callback.onLoginError(errorMessage));
                    }
                });
            }
        });
    }
    // Helper method to run code on the main UI thread
    private void runOnUiThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }

    // Method to perform register operation
    public void registerUser(RegisterRequest registerRequest, final RegisterCallback callback) {
        apiService.registerUser(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    String profilePictureUrl = registerResponse.getProfilePictureUrl();
                    // Save user data to SharedPreferences
                    saveToken(registerResponse.getToken());
                    saveProfilePicture(profilePictureUrl);

                    // Save to Room database
                    UserEntity userEntity = new UserEntity(registerRequest.getUsername(), registerRequest.getPassword(), registerResponse.getToken(), profilePictureUrl);
                    userEntity.setUsername(registerRequest.getUsername());
                    userEntity.setToken(registerResponse.getToken());
                    userEntity.setProfilePictureUrl(profilePictureUrl);
                    UserRoomDatabase.databaseWriteExecutor.execute(() -> {
                        userDao.insert(userEntity);
                    });

                    // Pass the register response to ViewModel
                    callback.onRegisterResponse(registerResponse);
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to register: " + response.message();
                    Log.e("UserRepository", errorMessage);
                    callback.onRegisterError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                String errorMessage = "Network error. Please try again later.";
                Log.e("UserRepository", errorMessage, t);
                callback.onRegisterError(errorMessage);
            }
        });
    }

    // Method to perform delete user operation
    public void deleteUser(String username, final DeleteUserCallback callback) {
        String token = sharedPreferences.getString("jwtToken", "");
        String authHeader = "Bearer " + token;
        apiService.deleteUser(authHeader, username).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Clear user data from SharedPreferences
                    clearUserData();
                    // Clear user data from Room database for the specific user
                    UserRoomDatabase.databaseWriteExecutor.execute(() -> {
                        userDao.deleteByUsername(username);
                    });
                    // Pass the success response to ViewModel
                    callback.onDeleteUserResponse();
                } else {
                    // Handle other HTTP error codes
                    String errorMessage = "Failed to delete account: " + response.message();
                    Log.e("UserRepository", errorMessage);
                    callback.onDeleteUserError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String errorMessage = "Network error. Please try again later.";
                Log.e("UserRepository", errorMessage, t);
                callback.onDeleteUserError(errorMessage);
            }
        });
    }

    // Method to fetch user data from Room
    public LiveData<UserEntity> getUserFromRoom() {
        Log.d("UserRepository", "Retrieving user from Room");
        return userDao.getUser(getLoggedInUsername());
    }
    // Helper method to get logged in username from SharedPreferences
    private String getLoggedInUsername() {
        return sharedPreferences.getString("currentUser", "");
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
    public interface LoginCallback {
        void onLoginResponse(LoginResponse response);
        void onLoginError(String errorMessage);
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
