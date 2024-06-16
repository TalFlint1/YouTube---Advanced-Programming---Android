package com.example.youtube_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youtubeandroid.R;

public class MainActivity extends AppCompatActivity {

    private Button viewVideoButton;
    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewVideoButton = findViewById(R.id.viewVideoButton);

        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity(v);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity(v);
            }
        });


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
}
    public void openRegisterActivity(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void openLoginActivity(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}