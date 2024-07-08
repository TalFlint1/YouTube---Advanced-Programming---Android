package com.example.youtube_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.youtubeandroid.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends AppCompatActivity {

    private ImageButton homepageButton;
    private ImageButton loginButton;
    private ImageButton addContentButton;
    private ImageButton searchButton;
    private ImageButton darkmodeButton;
    private HomePageFragment homePageFragment;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load user preference for dark mode
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main_page);

        // Initialize ApiService using RetrofitClient
        apiService = RetrofitClient.getApiService();

        homepageButton = findViewById(R.id.homepageButton);
        loginButton = findViewById(R.id.loginButton);
        addContentButton = findViewById(R.id.addContentButton);
        searchButton = findViewById(R.id.searchButton);
        darkmodeButton = findViewById(R.id.darkmodeButton);

        // Set default fragment
        homePageFragment = new HomePageFragment();
        loadFragment(homePageFragment);

        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(homePageFragment);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        addContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, AddVideoActivity.class);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homePageFragment != null) {
                    homePageFragment.toggleSearchBar();
                }
            }
        });

        darkmodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDarkMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveDarkModePreference(false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveDarkModePreference(true);
                }
            }
        });

        // Example: Perform login and load user data
        String username = sharedPreferences.getString("currentUser", "");
        String token = sharedPreferences.getString("jwtToken", "");

        if (!username.isEmpty() && !token.isEmpty()) {
            // Perform login using Retrofit API service
            LoginRequest loginRequest = new LoginRequest(username, "");
            Call<LoginResponse> call = apiService.loginUser(loginRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        String profilePictureUrl = loginResponse.getProfilePictureUrl();
                        // Save user data to SharedPreferences
                        saveToken(loginResponse.getToken());
                        saveProfilePicture(profilePictureUrl);
                        // Log the SharedPreferences values
                        displaySharedPreferences();
                    } else {
                        // Handle unsuccessful login (e.g., invalid credentials)
                        Log.e("MainPage", "Failed to login: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    // Handle network errors
                    Log.e("MainPage", "Error logging in: " + t.getMessage());
                }
            });
        }
    }

    private void displaySharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("currentUser", "");
        String token = sharedPreferences.getString("jwtToken", "");
        String profilePictureUrl = sharedPreferences.getString("profilePictureUrl", "");

        Log.d("SharedPreferences", "Username: " + username);
        Log.d("SharedPreferences", "Token: " + token);
        Log.d("SharedPreferences", "ProfilePictureUrl: " + profilePictureUrl);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void saveDarkModePreference(boolean isDarkMode) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dark_mode", isDarkMode);
        editor.apply();
    }

    private void saveToken(String token) {
        // Save token in SharedPreferences or any other secure storage method
        // Example using SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("jwtToken", token);
        editor.apply();
    }

    private void saveProfilePicture(String profilePictureUrl) {
        // Save profile picture URL in SharedPreferences or any other storage method
        // Example using SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("profilePictureUrl", profilePictureUrl);
        editor.apply();
    }
}
