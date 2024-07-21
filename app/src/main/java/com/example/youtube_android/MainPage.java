package com.example.youtube_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.youtubeandroid.R;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;
public class MainPage extends AppCompatActivity {

    private ImageButton homepageButton;
    private ImageButton loginButton;
    private ImageButton signoutButton;
    private ImageButton addContentButton;
    private ImageButton searchButton;
    private ImageButton darkmodeButton;
    private HomePageFragment homePageFragment;
    private ApiService apiService;
    private TextView profileButton;
    private UserRepository userRepository;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        userRepository = new UserRepository(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Initialize ApiService using RetrofitClient
        apiService = RetrofitClient.getApiService();

        homepageButton = findViewById(R.id.homepageButton);
        loginButton = findViewById(R.id.loginButton);
        signoutButton = findViewById(R.id.signoutButton);
        addContentButton = findViewById(R.id.addContentButton);
        searchButton = findViewById(R.id.searchButton);
        darkmodeButton = findViewById(R.id.darkmodeButton);
        profileButton = findViewById(R.id.profileButton);
        // Set default fragment
        homePageFragment = new HomePageFragment();
        loadFragment(homePageFragment);

        // Observe user data changes from Room
        userViewModel.getUser().observe(this, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity != null) {
                    // Update UI based on logged-in state
                    updateUI(userEntity.getUsername());
                } else {
                    // No user logged in, show default UI
                    updateUI(null);
                }
            }
        });

        // Load user preference for dark mode
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isSignedIn", false);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if (isLoggedIn) {
            loginButton.setVisibility(View.GONE); // Hide login button if logged in
            signoutButton.setVisibility(View.VISIBLE); // Show sign-out button if logged in
            // Set profile button
            String currentUser = sharedPreferences.getString("currentUser", "");
            if (!currentUser.isEmpty()) {
                String initial = String.valueOf(currentUser.charAt(0)).toUpperCase();
                profileButton.setText(initial);
                profileButton.setVisibility(View.VISIBLE); // Show profile button if logged in
            }
        } else {
            loginButton.setVisibility(View.VISIBLE); // Show login button if not logged in
            signoutButton.setVisibility(View.GONE); // Hide sign-out button if not logged in
            profileButton.setVisibility(View.GONE); // Hide profile button if not logged in
        }
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle sign-out logic
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("jwtToken");
                editor.remove("currentUser");
                editor.remove("currentPass");
                editor.putBoolean("isSignedIn", false);
                editor.apply();

                // Update UI after sign-out
                loginButton.setVisibility(View.VISIBLE);
                signoutButton.setVisibility(View.GONE);
                profileButton.setVisibility(View.GONE);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

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

        addContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, AddVideoActivity.class);
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

        darkmodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDarkMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveDarkModePreference(false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveDarkModePreference(true);
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

    private void saveDarkModePreference(boolean isDarkMode) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dark_mode", isDarkMode);
        editor.apply();
    }

    private void updateUI(String username) {
        if (username != null && !username.isEmpty()) {
            // User is logged in, update UI accordingly
            loginButton.setVisibility(View.GONE);
            signoutButton.setVisibility(View.VISIBLE);
            profileButton.setText(String.valueOf(username.charAt(0)).toUpperCase());
            profileButton.setVisibility(View.VISIBLE);
        } else {
            // User is not logged in, show default UI
            loginButton.setVisibility(View.VISIBLE);
            signoutButton.setVisibility(View.GONE);
            profileButton.setVisibility(View.GONE);
        }
    }
}