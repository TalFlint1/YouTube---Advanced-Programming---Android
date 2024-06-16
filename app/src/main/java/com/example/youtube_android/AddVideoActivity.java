package com.example.youtube_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class AddVideoActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_VIDEO = 2;
    private static final int GALLERY_PERMISSION_CODE = 102;

    private EditText titleEditText, urlEditText;
    private Button addVideoButton, selectVideoButton;
    private ImageView videoPreview;
    private Uri selectedVideoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        // Find the close button
        ImageButton closeButton = findViewById(R.id.closeButton);

        // Set click listener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main page (MainActivity)
                startActivity(new Intent(AddVideoActivity.this, MainActivity.class));
                finish(); // Finish the current activity to prevent going back to this page
            }
        });

        titleEditText = findViewById(R.id.title);
        urlEditText = findViewById(R.id.url);
        addVideoButton = findViewById(R.id.addVideoButton);
        selectVideoButton = findViewById(R.id.selectVideoButton);
        videoPreview = findViewById(R.id.videoPreview);

        addVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String url = urlEditText.getText().toString();

                if (validateFields(title, url, selectedVideoUri)) {
                    // Handle video upload logic here
                    Toast.makeText(AddVideoActivity.this, "Video added successfully!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(AddVideoActivity.this, MainActivity.class));
                    finish(); // Finish the current activity to prevent going back to this page
                }
            }
        });

        selectVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for permission to access the gallery
                if (ContextCompat.checkSelfPermission(AddVideoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddVideoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                } else {
                    dispatchPickVideoIntent();
                }
            }
        });
    }

    private void dispatchPickVideoIntent() {
        Intent pickVideoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickVideoIntent, REQUEST_PICK_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_VIDEO && data != null) {
            selectedVideoUri = data.getData();
            if (selectedVideoUri != null) {
                try {
                    Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(getRealPathFromUri(selectedVideoUri), MediaStore.Video.Thumbnails.MINI_KIND);
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


    private boolean validateFields(String title, String url, Uri videoUri) {
        boolean isValid = true;

        if (title.isEmpty()) {
            titleEditText.setError("Title is required");
            isValid = false;
        }
        if (url.isEmpty() && videoUri == null) {
            urlEditText.setError("Either URL or video file is required");
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchPickVideoIntent();
            } else {
                Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
