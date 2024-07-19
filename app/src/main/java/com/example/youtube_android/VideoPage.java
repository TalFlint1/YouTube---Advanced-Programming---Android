


package com.example.youtube_android;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
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
    private VideoRepository repository;

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
    private Video video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_page);
        repository = new VideoRepository();

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
        int id = getIntent().getIntExtra("id", 0);

        // Set video details
        videoTitle.setText(title);
        videoUsername.setText(username);
        videoViews.setText(views);
        videoTime.setText(time);
        likes.setText("" + likeCount + " לייקים"); // Set the like count

        video = new Video(title,username,views,time,videoUrl,likeCount,id);


        // Ensure the video URL is valid
        if (videoUrl != null && !videoUrl.isEmpty()) {
            //Uri uri = getResourceUri(videoUrl);
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.v4);
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
        commentsAdapter = new CommentsAdapter(commentsList, video);
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
                repository.GetVideo( video,new VideoRepository.GetVideoCallback() {


                    @Override
                    public void onGetVideoResponse(Video response) {
                        video = response;
                        video.setLikes(likeCount);
                        video.setLiked(isLiked);
                        repository.UpdateVideo( video,new VideoRepository.UpdateVideosCallback() {

                            @Override
                            public void onUpdateVideosResponse(Video response) {

                                Log.i("response:  ", response.toString());
                                System.out.println( response);

                            }

                            @Override
                            public void onUpdateVideosError(String errorMessage) {
                                // Handle error
                                //android.widget.Toast.makeText(Hom.this, "Failed to delete account: " + errorMessage, android.widget.Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onGetVideoError(String errorMessage) {

                    }
                });

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
                Log.i("comment:  ", comment);
                Log.i("video:  ", video.getTitle());
                repository.GetVideo( video,new VideoRepository.GetVideoCallback() {


                    @Override
                    public void onGetVideoResponse(Video response) {
                        video = response;
                        Comment c = new Comment(comment , video.getId());
                        video.addComment(c);
                        repository.UpdateVideo( video,new VideoRepository.UpdateVideosCallback() {

                            @Override
                            public void onUpdateVideosResponse(Video response) {

                                Log.i("response:  ", response.toString());
                                System.out.println( response);

                            }

                            @Override
                            public void onUpdateVideosError(String errorMessage) {
                                // Handle error
                                //android.widget.Toast.makeText(Hom.this, "Failed to delete account: " + errorMessage, android.widget.Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onGetVideoError(String errorMessage) {

                    }
                });

            }
        });

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