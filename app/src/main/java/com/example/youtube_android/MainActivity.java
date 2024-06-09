package com.example.youtube_android;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private TextView videoTitle;
    private TextView videoViews;
    private TextView videoTime;
    private TextView videoUsername;
    private TextView videoUrlText;
    private ImageView userIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        videoUrlText.setText(videoUrl);  // Set the video URL text
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            reader.close();

//            if (videoUrl != null) {
//                Uri uri = Uri.parse(videoUrl);
//                videoView.setVideoURI(uri);
//                videoView.start();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
