package com.example.youtube_android;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private Button likeButton;
    private EditText commentEditText;

    private Button commentButton;
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private List<String> commentsList;
    private int likeCount = 0;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.video_view);
        likeButton = findViewById(R.id.like_button);
        commentEditText = findViewById(R.id.comment_edit_text);
        commentButton = findViewById(R.id.comment_button);
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);

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
                likeCount++;
                likeButton.setText("Like (" + likeCount + ")");
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
    }
}

