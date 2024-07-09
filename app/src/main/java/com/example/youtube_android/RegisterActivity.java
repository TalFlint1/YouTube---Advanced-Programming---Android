package com.example.youtube_android;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.youtubeandroid.R;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int GALLERY_PERMISSION_CODE = 102;

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, nameEditText;
    private Button registerButton, selectPictureButton;
    private ImageView imagePreview;
    private ImageView passwordHelpIcon;
    private TextView passwordTooltip;
    private RegisterViewModel registerViewModel;
    private SharedPreferences sharedPreferences;

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
                startActivity(new Intent(RegisterActivity.this, MainPage.class));
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
        passwordHelpIcon = findViewById(R.id.passwordHelpIcon);
        passwordTooltip = findViewById(R.id.passwordTooltip);

        // Set click listener for the password help icon
        passwordHelpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the visibility of the tooltip
                if (passwordTooltip.getVisibility() == View.GONE) {
                    passwordTooltip.setVisibility(View.VISIBLE);
                } else {
                    passwordTooltip.setVisibility(View.GONE);
                }
            }
        });

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        registerViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(RegisterViewModel.class);
        registerViewModel.getRegisterResponse().observe(this, registerResponse -> {
            if (registerResponse != null) {
                String token = registerResponse.getToken();
                String profilePictureUrl = registerResponse.getProfilePictureUrl();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("jwtToken", token);
                editor.putString("currentUser", usernameEditText.getText().toString());
                editor.putString("currentPass", passwordEditText.getText().toString());
                editor.putString("profilePictureUrl", profilePictureUrl);
                editor.putBoolean("isSignedIn", true);
                editor.apply();

                Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, MainPage.class));
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String profilePictureBase64 = null;
                if (imagePreview.getDrawable() != null) {
                    Bitmap profilePicture = ((BitmapDrawable) imagePreview.getDrawable()).getBitmap();
                    Bitmap resizedProfilePicture = resizeBitmap(profilePicture, 200, 200); // Resize the bitmap to 200x200
                    profilePictureBase64 = convertBitmapToBase64(resizedProfilePicture);
                    Toast.makeText(RegisterActivity.this, "hola"
                            , Toast.LENGTH_SHORT).show();
                }
//                Bitmap profilePicture = imagePreview.getDrawable() != null ? ((BitmapDrawable) imagePreview.getDrawable()).getBitmap() : null;

                if (validateFields(username, password, confirmPassword, name)) {
                    // Save the user data using SharedPreferences
                    RegisterRequest registerRequest = new RegisterRequest(username, password, name, profilePictureBase64);
                    registerViewModel.register(registerRequest);
                    Toast.makeText(RegisterActivity.this, "Registration successful!"
                            , Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(RegisterActivity.this, MainPage.class));
                    finish(); // Finish the current activity to prevent going back to the registration page
                }
            }
        });

        selectPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for permission to access the gallery
                if (ContextCompat.checkSelfPermission(RegisterActivity.this
                        , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this
                            , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                } else {
                    showPictureDialog();
                }
            }
        });
    }

    // Method to resize the Bitmap
    private Bitmap resizeBitmap(Bitmap original, int maxWidth, int maxHeight) {
        int width = original.getWidth();
        int height = original.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxWidth;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxHeight;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(original, width, height, true);
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void showPictureDialog() {
        // Display options to either take a photo or choose from gallery
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                // Check for camera permission
                if (ContextCompat.checkSelfPermission(RegisterActivity.this
                        , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this
                            , new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                } else {
                    dispatchTakePictureIntent();
                }
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
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
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
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imagePreview.setImageBitmap(imageBitmap);
                imagePreview.setVisibility(View.VISIBLE);
            } else if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                // Show the image from the gallery
                Uri selectedImage = data.getData();
                imagePreview.setImageURI(selectedImage);
                imagePreview.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean validateFields(String username, String password, String confirmPassword, String name) {
        boolean isValid = true;

        // Check if any field is empty and display error messages accordingly
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            isValid = false;
        }
        if (password.length() < 8 || password.length() > 16
                || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*]{8,16}$")) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPictureDialog();
            } else {
                Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
