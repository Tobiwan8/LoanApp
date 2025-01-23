package com.example.loanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private boolean isDarkModeEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnToggleTheme = findViewById(R.id.btnToggleTheme);
        Button btnLoan = findViewById(R.id.btnLoan);
        Button btnHandIn = findViewById(R.id.btnHandIn);
        Button btnAdminLogin = findViewById(R.id.btnAdminLogin);

        // Set up theme toggle functionality
        btnToggleTheme.setOnClickListener(v -> {
            if (isDarkModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Light mode
                isDarkModeEnabled = false;
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Dark mode
                isDarkModeEnabled = true;
            }
        });

        // Navigate to BorrowActivity
        btnLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BorrowActivity.class));
            }
        });

        // Navigate to HandInActivity
        btnHandIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HandInActivity.class));
            }
        });

        // Navigate to LoginActivity
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}
