package com.example.youtube_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.youtubeandroid.R;

public class MainPage extends AppCompatActivity {

    private ImageButton homepageButton;
    private ImageButton loginButton;

    //    private ImageButton addContentButton;

    private ImageButton videoPageButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        homepageButton = findViewById(R.id.homepageButton);
        loginButton = findViewById(R.id.loginButton);

//        addContentButton = findViewById(R.id.addContentButton);

        videoPageButton = findViewById(R.id.videoPageButton);




        homepageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                homepageActivity(v);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivity(v);
            }
        });


//        addContentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addContentActivity(v);
//            }
//        });

        videoPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPageActivity(v);
            }
        });
    }

    public void homepageActivity(View view) {
        Intent intent = new Intent(MainPage.this, HomePage.class);
        startActivity(intent);
    }
    public void loginActivity(View view) {
        Intent intent = new Intent(MainPage.this, LoginActivity.class);
        startActivity(intent);
    }

//    public void addContentActivity(View view) {
//        Intent intent = new Intent(MainPage.this, AddContentPage.class);
//        startActivity(intent);
//    }

    public void videoPageActivity(View view) {
        Intent intent = new Intent(MainPage.this, VideoPage.class);
        startActivity(intent);
    }
}
