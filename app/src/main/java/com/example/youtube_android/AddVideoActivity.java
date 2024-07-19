package com.example.youtube_android;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.youtubeandroid.R;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AddVideoActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_PICK_VIDEO = 2;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int GALLERY_PERMISSION_CODE = 102;
    private VideoRepository repository;

    private EditText titleEditText;
    private Button addVideoButton, selectVideoButton;
    private ImageView videoPreview;
    private Uri selectedVideoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        repository = new VideoRepository();
        // Find the close button
        ImageButton closeButton = findViewById(R.id.closeButton);

        // Set click listener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main page (MainActivity)
                startActivity(new Intent(AddVideoActivity.this, MainPage.class));
                finish(); // Finish the current activity to prevent going back to this page
            }
        });

        titleEditText = findViewById(R.id.title);
        addVideoButton = findViewById(R.id.addVideoButton);
        selectVideoButton = findViewById(R.id.selectVideoButton);
        videoPreview = findViewById(R.id.videoPreview);

        addVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();

                if (validateFields(title, selectedVideoUri)) {
                    // Handle video upload logic here
                    Video newItem = new Video(title,"me","0","0",selectedVideoUri.getPath(),0);
                    Toast.makeText(AddVideoActivity.this, "Video added successfully!", Toast.LENGTH_SHORT).show();
                    repository.CreateVideo(newItem, new VideoRepository.CreateVideosCallback() {
                        @Override
                        public void onCreateVideosResponse(Video response) {
                            Log.i("ok","ok");

                        }

                        @Override
                        public void onCreateVideosError(String errorMessage) {
                            // Handle error
                        }
                    });
                    startActivity(new Intent(AddVideoActivity.this, MainPage.class));
                    finish(); // Finish the current activity to prevent going back to this page
                }
            }
        });

        selectVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideoDialog();
            }
        });
    }

    private void showVideoDialog() {
        final CharSequence[] options = {"Take Video", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddVideoActivity.this);
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Video")) {
                // Check for camera permission
                if (ContextCompat.checkSelfPermission(AddVideoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddVideoActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                } else {
                    dispatchTakeVideoIntent();
                }
            } else if (options[item].equals("Choose from Gallery")) {
                // Check for permission to access the gallery
                if (ContextCompat.checkSelfPermission(AddVideoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddVideoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                } else {
                    dispatchPickVideoIntent();
                }
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        try {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchPickVideoIntent() {
        Intent pickVideoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickVideoIntent, REQUEST_PICK_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CAPTURE && data != null) {
                selectedVideoUri = data.getData();
                if (selectedVideoUri != null) {
                    setVideoPreview(selectedVideoUri);
                }
            } else if (requestCode == REQUEST_PICK_VIDEO && data != null) {
                selectedVideoUri = data.getData();
                if (selectedVideoUri != null) {
                    setVideoPreview(selectedVideoUri);
                }
            }
        }
    }

    private void setVideoPreview(Uri videoUri) {
        try {
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(getRealPathFromUri(videoUri), MediaStore.Video.Thumbnails.MINI_KIND);
            if (thumbnail != null) {
                videoPreview.setImageBitmap(thumbnail);
                videoPreview.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Failed to load video thumbnail", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load video thumbnail", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to get the real path from URI (for Android 10 and above)
    private String getRealPathFromUri(Uri uri) throws IOException {
        String realPath;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    realPath = cursor.getString(index);
                    return realPath;
                }
            }
        }
        return uri.getPath();
    }

    private boolean validateFields(String title, Uri videoUri) {
        boolean isValid = true;

        if (title.isEmpty()) {
            titleEditText.setError("Title is required");
            isValid = false;
        }
        if (videoUri == null) {
            Toast.makeText(this, "Video is required", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakeVideoIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchPickVideoIntent();
            } else {
                Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}