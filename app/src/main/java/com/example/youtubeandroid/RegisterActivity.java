package com.example.youtubeandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, nameEditText;
    private Button registerButton, selectPictureButton;
    private ImageView imagePreview;
    private String currentPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Find the close button
        ImageButton closeButton = findViewById(R.id.closeButton);

        // Set click listener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main page (MainActivity)
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish(); // Finish the current activity to prevent going back to the registration page
            }
        });

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        nameEditText = findViewById(R.id.name);
        registerButton = findViewById(R.id.registerButton);
        selectPictureButton = findViewById(R.id.selectPictureButton);
        imagePreview = findViewById(R.id.imagePreview);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String name = nameEditText.getText().toString();

                if (validateFields(username, password, confirmPassword, name)) {
                    // Save the user data using SharedPreferences
                    saveUserData(username, password, name);
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                    // Verify that the data is saved
                    verifyUserData();

                    // Example: Redirect to a welcome screen or the main activity after registration
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish(); // Finish the current activity to prevent going back to the registration page
                } else {
                    // Display error message if validation fails
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
    }

    private void showPictureDialog() {
        // Display options to either take a photo or choose from gallery
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                dispatchTakePictureIntent();
            } else if (options[item].equals("Choose from Gallery")) {
                dispatchPickPictureIntent();
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.youtubeandroid.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void dispatchPickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPictureIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Show the image from the camera
                File file = new File(currentPhotoPath);
                imagePreview.setImageURI(Uri.fromFile(file));
                imagePreview.setVisibility(View.VISIBLE);
            } else if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                // Show the image from the gallery
                Uri selectedImage = data.getData();
                imagePreview.setImageURI(selectedImage);
                imagePreview.setVisibility(View.VISIBLE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Example method for validating input fields
    private boolean validateFields(String username, String password, String confirmPassword, String name) {
        boolean isValid = true;

        // Check if any field is empty and display error messages accordingly
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            isValid = false;
        }
        if (password.length() < 8 || password.length() > 16 || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*]{8,16}$")) {
            passwordEditText.setError("Password must be between 8-16 characters and combine letters and numbers");
            isValid = false;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Please confirm your password");
            isValid = false;
        }
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            isValid = false;
        }
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            isValid = false;
        }

        return isValid;
    }

    // Method to save user data using SharedPreferences
    private void saveUserData(String username, String password, String name) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("name", name);
        editor.apply();
    }

    // Method to retrieve user data from SharedPreferences
    private void verifyUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        String name = sharedPreferences.getString("name", "");

        // Display the retrieved data as a Toast message for verification
        Toast.makeText(this, "Saved Data - Username: " + username + ", Name: " + name, Toast.LENGTH_LONG).show();
    }
}
