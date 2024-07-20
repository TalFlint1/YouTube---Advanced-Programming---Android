package com.example.youtube_android;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.youtubeandroid.R;

public class ProfileActivity extends AppCompatActivity {
    private UserRepository userRepository;
    private String currentUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UserRepository
        userRepository = new UserRepository(this);

        // Find the close button
        ImageButton closeButton = findViewById(R.id.closeButton);
        TextView userText = findViewById(R.id.userText);
        TextView passText = findViewById(R.id.passText);
        ImageView profileImage = findViewById(R.id.profileImage);
        Button deleteAccountButton = findViewById(R.id.deleteButton);
        TextView errorText = findViewById(R.id.errorText);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        currentUser = sharedPreferences.getString("currentUser", "");
        String currentPass = sharedPreferences.getString("currentPass", "");
        String profilePictureBase64 = sharedPreferences.getString("profilePictureUrl", "");

        userText.setText(currentUser);
        passText.setText(currentPass);

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

        // Set click listener for the delete account button
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }
    private void showDeleteConfirmationDialog() {
        // Define the options for the dialog
        final CharSequence[] options = {"Yes", "Cancel"};

        // Create and configure the AlertDialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Are you sure you want to delete your account?");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Yes")) {
                deleteUserAccount();
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        builder.show();
    }

    private void deleteUserAccount() {
        userRepository.deleteUser(currentUser, new UserRepository.DeleteUserCallback() {
            @Override
            public void onDeleteUserResponse() {
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("jwtToken");
                editor.remove("currentUser");
                editor.remove("currentPass");
                editor.putBoolean("isSignedIn", false);
                editor.apply();
                // Account deleted successfully, navigate to login activity
                startActivity(new Intent(ProfileActivity.this, MainPage.class));
                finish();
            }

            @Override
            public void onDeleteUserError(String errorMessage) {
                // Display error message in red
                TextView errorText = findViewById(R.id.errorText);
                errorText.setText(errorMessage);
                errorText.setVisibility(View.VISIBLE);
            }
        });
    }
}

