package com.example.youtube_android;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // on below line we are creating variables.
    private VideoView videoView;

    // Your Video URL
    String videoUrl = "https://www.youtube.com/watch?v=aqz-KE-bpKQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView videoView = findViewById(R.id.video_view);
        Uri uri = Uri.parse("android.resource://"+ getPackageName() + "/" + R.raw.v3);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }
}
