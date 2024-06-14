package com.example.youtube_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button viewVideoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewVideoButton = findViewById(R.id.viewVideoButton);


        viewVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewVideo(v);
            }
        });
    }

    public void openViewVideo(View view) {
        Intent intent = new Intent(MainActivity.this, VideoPage.class);
        startActivity(intent);
}}