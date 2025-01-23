package com.example.loanapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import com.example.loanapp.model.ItemModel;
import com.example.loanapp.model.LoanModel;
import com.example.loanapp.model.UserModel;
import com.example.loanapp.data.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class BorrowActivity extends AppCompatActivity {

    private RadioGroup radioGroupItemType;
    private Spinner spinnerItem;
    private ToggleButton toggleExistingUser;
    private LinearLayout newUserLayout;
    private EditText etFullName, etPhoneNumber, etEmail, etLoanNumber;
    private Button btnCreateLoan;

    private String selectedItemType = "Tablet"; // Default to Tablet
    private String selectedItemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        // Initialize UI components
        radioGroupItemType = findViewById(R.id.radioGroupItemType);
        spinnerItem = findViewById(R.id.spinnerItem);
        toggleExistingUser = findViewById(R.id.toggleExistingUser);
        newUserLayout = findViewById(R.id.newUserLayout);
        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        etLoanNumber = findViewById(R.id.etLoanNumber);
        btnCreateLoan = findViewById(R.id.btnCreateLoan);

        // Set default item list
        updateSpinnerOptions(selectedItemType);

        // Listener for item type selection
        radioGroupItemType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioTablet) {
                selectedItemType = "Tablet";
            } else {
                selectedItemType = "Cable";
            }
            updateSpinnerOptions(selectedItemType);
        });

        // Listener for toggle button
        toggleExistingUser.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                newUserLayout.setVisibility(View.GONE);
                etLoanNumber.setVisibility(View.VISIBLE);
            } else {
                newUserLayout.setVisibility(View.VISIBLE);
                etLoanNumber.setVisibility(View.GONE);
            }
        });

        // Listener for loan creation
        btnCreateLoan.setOnClickListener(v -> createLoan());
    }

    // Update spinner options based on item type
    private void updateSpinnerOptions(String itemType) {
        String[] options;
        if (itemType.equals("Tablet")) {
            options = new String[]{"Samsung", "Apple", "Lenovo"};
        } else {
            options = new String[]{"USB-C", "HDMI"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItem.setAdapter(adapter);

        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemName = options[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedItemName = null;
            }
        });
    }

    // Create loan logic
    private void createLoan() {
        if (toggleExistingUser.isChecked()) {
            // Existing user loan creation
            String loanNumber = etLoanNumber.getText().toString().trim();
            if (loanNumber.isEmpty()) {
                Toast.makeText(this, "Venligst indtast et gyldigt lånenummer", Toast.LENGTH_SHORT).show();
                return;
            }
            // Simulate finding user by loan number and creating loan
            Toast.makeText(this, "Lån oprettet!", Toast.LENGTH_SHORT).show();
        } else {
            // New user registration and loan creation
            String fullName = etFullName.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (fullName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Venligst udfylt alle informationer", Toast.LENGTH_SHORT).show();
                return;
            }

            UserModel newUser = new UserModel(12345, fullName, phoneNumber, email); // Simulate auto-generated loan number
            LoanModel newLoan = new LoanModel(new ItemModel(selectedItemType, selectedItemName), new Date());
            newUser.getLoans().add(newLoan);

            // Simulate saving user and loan to JSON
            Toast.makeText(this, "Lån oprettet!", Toast.LENGTH_SHORT).show();
        }

        // Navigate back to MainActivity
        finish();
    }
}
