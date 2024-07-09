package com.example.youtube_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youtubeandroid.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Find the close button
        ImageButton closeButton = findViewById(R.id.closeButton);
        TextView userText = findViewById(R.id.userText);
        TextView passText = findViewById(R.id.passText);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        String currentUser = sharedPreferences.getString("currentUser", "");
        String currenPass = sharedPreferences.getString("currentPass", "");

        userText.setText(currentUser);
        passText.setText(currenPass);

        // Set click listener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main page (MainActivity)
                startActivity(new Intent(ProfileActivity.this, MainPage.class));
                finish(); // Finish the current activity to prevent going back to the login page
            }
        });
    }
}

