package com.example.youtube_android;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;

public class VideoPage extends AppCompatActivity {

    private VideoView videoView;
    private ImageButton likeButton;
    private EditText commentEditText;
    private ImageButton commentButton;

    // private Button commentButton;
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
    private TextView videoUrlText;
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
        likes= findViewById(R.id.likes);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.v3);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

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
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            reader.beginObject();

            String videoUrl = null;

            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "title":
                        videoTitle.setText(reader.nextString());
                        break;
                    case "views":
                        videoViews.setText(reader.nextString());
                        break;
                    case "time":
                        videoTime.setText(reader.nextString());
                        break;
                    case "username":
                        videoUsername.setText(reader.nextString());
                        break;
                    case "video_url":
                        videoUrl = reader.nextString();
                        break;
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
}
