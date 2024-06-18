package com.example.youtube_android;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeandroid.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class VideoPage extends AppCompatActivity {

    private VideoView videoView;
    private ImageButton likeButton;
    private EditText commentEditText;
    private ImageButton commentButton;

    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private List<String> commentsList;
    private int likeCount = 0;
    private boolean isLiked = false;

    private TextView videoTitle;
    private TextView videoViews;
    private TextView videoTime;
    private TextView videoUsername;
    private TextView likes;
    private ImageView userIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_page);

        videoView = findViewById(R.id.video_view);
        likeButton = findViewById(R.id.like_button);
        commentEditText = findViewById(R.id.comment_edit_text);
        commentButton = findViewById(R.id.comment_button);
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        videoTitle = findViewById(R.id.video_title);
        videoViews = findViewById(R.id.video_views);
        videoTime = findViewById(R.id.video_time);
        videoUsername = findViewById(R.id.video_username);
        userIcon = findViewById(R.id.user_icon);
        likes = findViewById(R.id.likes);

        // Get video details from intent
        String title = getIntent().getStringExtra("video_title");
        String username = getIntent().getStringExtra("video_username");
        String views = getIntent().getStringExtra("video_views");
        String time = getIntent().getStringExtra("video_time");
        String videoUrl = getIntent().getStringExtra("video_url");
        likeCount = getIntent().getIntExtra("like_count", 0);

        // Set video details
        videoTitle.setText(title);
        videoUsername.setText(username);
        videoViews.setText(views);
        videoTime.setText(time);
        likes.setText("" + likeCount + " לייקים"); // Set the like count




        // Ensure the video URL is valid
        if (videoUrl != null && !videoUrl.isEmpty()) {
            Uri uri = getResourceUri(videoUrl);
            //Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.v3);
            videoView.setVideoURI(uri);
            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
//            videoView.requestFocus();
//            videoView.start();
        } else {
            // Handle invalid video URL
            // Show a message to the user or log the error
            videoTitle.setText("Invalid video URL");
        }

        commentsList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentsList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLiked) {
                    likeCount++;
                    isLiked = true;
                    likeButton.setImageResource(R.drawable.liked); // Change to filled like icon
                } else {
                    likeCount--;
                    isLiked = false;
                    likeButton.setImageResource(R.drawable.basic_l); // Change to regular like icon
                }
                likeButton.setContentDescription("" + likeCount + " likes");
                likes.setText("" + likeCount + " לייקים"); // Update the displayed like count
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEditText.getText().toString();
                if (!comment.isEmpty()) {
                    commentsList.add(comment);
                    commentsAdapter.notifyItemInserted(commentsList.size() - 1);
                    commentEditText.setText("");
                }
            }
        });

        loadJsonData();
    }

    private void loadJsonData() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.data);
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "likeCount":
                        likeCount = reader.nextInt();
                        likes.setText("" + likeCount + " לייקים"); // Update the displayed like count
                        break;
                    case "comments":
                        reader.beginArray();
                        while (reader.hasNext()) {
                            commentsList.add(reader.nextString());
                        }
                        reader.endArray();
                        commentsAdapter.notifyDataSetChanged();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Uri getResourceUri(String resourceName) {
        // Get the package name
        String packageName = getPackageName();
        // Get the resource ID from the resource name
        int resourceId = getResources().getIdentifier(resourceName, "raw", packageName);
        // Construct the URI
        Uri uri = Uri.parse("android.resource://" + packageName + "/" + resourceId);
        return uri;
    }

}