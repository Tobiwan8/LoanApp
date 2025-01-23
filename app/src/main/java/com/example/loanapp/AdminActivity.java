package com.example.loanapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.loanapp.model.LoanModel;
import com.example.loanapp.model.UserModel;
import com.example.loanapp.model.ItemModel;

public class AdminActivity extends AppCompatActivity {

    private EditText etSearch;
    private ListView lvUsersAndLoans;
    private Button btnLogout;

    private List<UserModel> allUsers; // Simulated data
    private ArrayAdapter<String> adapter;
    private List<String> displayList; // List of strings to display in ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize UI components
        etSearch = findViewById(R.id.etSearch);
        lvUsersAndLoans = findViewById(R.id.lvUsersAndLoans);
        btnLogout = findViewById(R.id.btnLogout);

        // Simulate loading all users from JSON
        allUsers = loadAllUsers();
        displayList = new ArrayList<>();

        // Populate the ListView with user data
        populateDisplayList(allUsers);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        lvUsersAndLoans.setAdapter(adapter);

        // Add search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Log out functionality
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(AdminActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Simulate loading all users from JSON
    private List<UserModel> loadAllUsers() {
        // Replace this with logic to read from JSON
        List<UserModel> mockUsers = new ArrayList<>();

        UserModel user1 = new UserModel(12345, "Alice Johnson", "1234567890", "alice@example.com");
        user1.getLoans().add(new LoanModel(new ItemModel("Tablet", "Samsung"), new java.util.Date()));

        UserModel user2 = new UserModel(67890, "Bob Smith", "0987654321", "bob@example.com");
        user2.getLoans().add(new LoanModel(new ItemModel("Cable", "HDMI"), new java.util.Date()));

        mockUsers.add(user1);
        mockUsers.add(user2);

        return mockUsers;
    }

    // Populate the display list for ListView
    private void populateDisplayList(List<UserModel> users) {
        displayList.clear();
        for (UserModel user : users) {
            StringBuilder userInfo = new StringBuilder();
            userInfo.append("Name: ").append(user.getFullName()).append("\n");
            userInfo.append("Phone: ").append(user.getPhoneNumber()).append("\n");
            userInfo.append("Email: ").append(user.getEmail()).append("\n");
            userInfo.append("Loans:\n");

            for (LoanModel loan : user.getLoans()) {
                userInfo.append(" - ").append(loan.getItem().getType()).append(": ").append(loan.getItem().getName()).append("\n");
            }
            displayList.add(userInfo.toString());
        }
    }

    // Filter users by name or loan number
    private void filterUsers(String query) {
        List<UserModel> filteredUsers = new ArrayList<>();

        for (UserModel user : allUsers) {
            if (user.getFullName().toLowerCase().contains(query.toLowerCase())
                    || String.valueOf(user.getLoanNumber()).contains(query)) {
                filteredUsers.add(user);
            }
        }

        populateDisplayList(filteredUsers);
        adapter.notifyDataSetChanged();
    }
}
