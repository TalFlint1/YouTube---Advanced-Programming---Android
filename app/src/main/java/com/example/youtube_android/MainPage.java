package com.example.youtube_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.youtubeandroid.R;

public class MainPage extends AppCompatActivity {

    private ImageButton homepageButton;
    private ImageButton loginButton;
    private ImageButton videoPageButton;
    private ImageButton searchButton;
    private HomePageFragment homePageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        homepageButton = findViewById(R.id.homepageButton);
        loginButton = findViewById(R.id.loginButton);
        videoPageButton = findViewById(R.id.videoPageButton);
        searchButton = findViewById(R.id.searchButton);

        // Set default fragment
        homePageFragment = new HomePageFragment();
        loadFragment(homePageFragment);

        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(homePageFragment);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        videoPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, VideoPage.class);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homePageFragment != null) {
                    homePageFragment.toggleSearchBar();
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
