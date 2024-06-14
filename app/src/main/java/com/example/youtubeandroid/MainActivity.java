package com.example.youtubeandroid;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.youtube_android.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // JSON data
        String jsonData = "{ \"id\":17,\"title\": \"Video 20\", \"description\": \"Description 1\", \"videoUrl\": \"https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4\", \"thumbnailUrl\": \"thumbnail1.jpg\", \"duration\": \"8:15\", \"owner\":\"צביקה ברגר\" , \"views\":721,\"time_publish\":4,\"time_type\":\"years\",\"user_icon\":\"https://yt3.ggpht.com/xqpDLeDfVG5K9w3VDXzQsg_0tvwqKp9Rg1QSc5d1XUfFTgXeHlQuqJ45ErN9qOCkLB2QwE2MnmE=s68-c-k-c0x00ffffff-no-rj\", \"likes\": 0, \"comments\": []}";

        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            // Extract video URL from JSON data
            String videoUrl = jsonObject.getString("videoUrl");

            // Set video source for VideoView
            VideoView videoView = findViewById(R.id.videoView);
            videoView.setVideoURI(Uri.parse(videoUrl));
            videoView.start(); // Start playing the video

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
