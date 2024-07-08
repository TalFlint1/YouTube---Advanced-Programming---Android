package com.example.youtube_android;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.example.youtubeandroid.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private LinearLayout errorContainer;
    private TextView errorText;
    private List<User> userList;
    private SharedPreferences sharedPreferences;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find the close button
        ImageButton closeButton = findViewById(R.id.closeButton);

        // Set click listener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main page (MainActivity)
                startActivity(new Intent(LoginActivity.this, MainPage.class));
                finish(); // Finish the current activity to prevent going back to the login page
            }
        });

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        errorContainer = findViewById(R.id.errorContainer);
        errorText = findViewById(R.id.errorText);
        registerButton = findViewById(R.id.registerButton);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        apiService = RetrofitClient.getApiService();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                performLogin(username, password);
            }
        });

        // Set click listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the RegisterActivity
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void performLogin(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<LoginResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("LoginRequest", "Username: " + loginRequest.getUsername());
                Log.d("LoginRequest", "Password: " + loginRequest.getPassword());
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getToken();
                    String profilePictureUrl = loginResponse.getProfilePictureUrl();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("jwtToken", token);
                    editor.putString("currentUser", username);
                    editor.putString("profilePictureUrl", profilePictureUrl);
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainPage.class));
                    finish();
                } else {
                    // Login failed, show error message
                    showError("Username or password are not correct. Please try again.");
                    Log.e(TAG, "Login failed with response code: " + response.code());
                    Log.e(TAG, "Response message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Login failed: " + t.getMessage());
                t.printStackTrace();
                // Handle network errors
                showError("Login error. Please try again later.");
            }
        });
    }

    private void showError(String message) {
        errorText.setText(message);
        errorContainer.setVisibility(View.VISIBLE);
    }
}
