package com.example.youtubeandroid;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, nameEditText;
    private Button registerButton;

    private ArrayList<User> users;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        nameEditText = findViewById(R.id.name);
        registerButton = findViewById(R.id.registerButton);

        users = new ArrayList<>();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String name = nameEditText.getText().toString();

                if (validateFields(username, password, confirmPassword, name)) {
                    // If all fields are valid, create a new user and add to the list
                    User newUser = new User(username, password, name);
                    users.add(newUser);

                    // Optionally, you can save the user data or perform further actions here
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                    // Example: Redirect to a welcome screen or the main activity after registration
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish(); // Finish the current activity to prevent going back to registration page
                }
            }
        });
    }

    // Example method for validating input fields
    private boolean validateFields(String username, String password, String confirmPassword, String name) {
        // Perform validation logic here (e.g., checking for empty fields, password match, etc.)
        // For simplicity, return true if all fields are non-empty
        return !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !name.isEmpty();
    }
}


