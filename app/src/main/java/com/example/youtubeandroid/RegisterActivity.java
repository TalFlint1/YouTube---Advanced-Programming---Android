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
                    finish(); // Finish the current activity to prevent going back to the registration page
                } else {
                    // Display error message if validation fails
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Example method for validating input fields
    private boolean validateFields(String username, String password, String confirmPassword, String name) {
        boolean isValid = true;

        // Check if any field is empty and display error messages accordingly
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            isValid = false;
        }
        if (password.length() < 8 || password.length() > 16 || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*]{8,16}$")) {
            passwordEditText.setError("Password must be between 8-16 characters and combine letters and numbers");
            isValid = false;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Please confirm your password");
            isValid = false;
        }
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            isValid = false;
        }
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            isValid = false;
        }

        return isValid;
    }


}
