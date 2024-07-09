package com.example.youtube_android;

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
import androidx.lifecycle.ViewModelProvider;
import com.example.youtubeandroid.R;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private LinearLayout errorContainer;
    private TextView errorText;
    private SharedPreferences sharedPreferences;
    private LoginViewModel loginViewModel;

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
        loginViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(LoginViewModel.class);
        loginViewModel.getLoginResponse().observe(this, loginResponse -> {
            if (loginResponse != null) {
                String token = loginResponse.getToken();
                String profilePictureUrl = loginResponse.getProfilePictureUrl();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("jwtToken", token);
                editor.putString("currentUser", usernameEditText.getText().toString());
                editor.putString("currentPass", passwordEditText.getText().toString());
                editor.putString("profilePictureUrl", profilePictureUrl);
                editor.putBoolean("isSignedIn", true);
                editor.apply();

                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainPage.class));
                finish();
            } else {
                showError("Login failed. Please try again");
            }
        });


        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            performLogin(username, password);
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void performLogin(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Log.d("LoginRequest", "Username: " + loginRequest.getUsername());
        Log.d("LoginRequest", "Password: " + loginRequest.getPassword());
        loginViewModel.login(loginRequest);
    }

    private void showError(String message) {
        errorText.setText(message);
        errorContainer.setVisibility(View.VISIBLE);
    }
}
