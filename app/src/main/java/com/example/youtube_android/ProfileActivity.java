package com.example.youtube_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        ImageView profileImage = findViewById(R.id.profileImage);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        String currentUser = sharedPreferences.getString("currentUser", "");
        String currenPass = sharedPreferences.getString("currentPass", "");
        String profilePictureBase64 = sharedPreferences.getString("profilePictureUrl", "");

        userText.setText(currentUser);
        passText.setText(currenPass);

        // Decode base64 string to bitmap
        if (!profilePictureBase64.isEmpty()) {
            byte[] decodedString = Base64.decode(profilePictureBase64, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Set bitmap to ImageView
            profileImage.setImageBitmap(decodedBitmap);
        }

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

