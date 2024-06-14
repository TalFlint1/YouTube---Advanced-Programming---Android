package com.example.youtube_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class toolbar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_toolbar);

        ImageButton btnHome = findViewById(R.id.btnHomePage);
        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(toolbar.this, mainPage.class);
            startActivity(i);
        });

        ImageButton btnRegister = findViewById(R.id.btnUser);
        btnRegister.setOnClickListener(v -> {
            Intent i = new Intent(toolbar.this, mainPage.class);
            startActivity(i);
        });

        ImageButton btnAddVid = findViewById(R.id.btnAddVid);
        btnRegister.setOnClickListener(v -> {
            Intent i = new Intent(toolbar.this, mainPage.class);
            startActivity(i);
        });

        // Use a different parameter name for this lambda expression
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
