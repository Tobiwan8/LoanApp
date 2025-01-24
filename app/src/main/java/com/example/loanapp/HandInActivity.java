package com.example.loanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.loanapp.data.DatabaseHelper;
import com.example.loanapp.model.LoanModel;
import com.example.loanapp.model.ItemModel;
import com.example.loanapp.model.UserModel;

public class HandInActivity extends AppCompatActivity {

    private EditText etLoanNumber;
    private Button btnSearchLoan, btnReturnLoan;
    private TextView tvLoanListHeader;
    private LinearLayout loanListLayout;

    private List<LoanModel> userLoans; // Holds the loans of the user
    private List<CheckBox> loanCheckBoxes; // Holds the dynamically created checkboxes
    private DatabaseHelper dbHelper; // DatabaseHelper instance to interact with SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_in);

        // Initialize UI components
        etLoanNumber = findViewById(R.id.etLoanNumber);
        btnSearchLoan = findViewById(R.id.btnSearchLoan);
        btnReturnLoan = findViewById(R.id.btnReturnLoan);
        tvLoanListHeader = findViewById(R.id.tvLoanListHeader);
        loanListLayout = findViewById(R.id.loanListLayout);

        userLoans = new ArrayList<>();
        loanCheckBoxes = new ArrayList<>();

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Search button functionality
        btnSearchLoan.setOnClickListener(v -> {
            hideKeyboard(); // Hide the keyboard
            searchLoans();  // Call the searchLoans method
        });

        // Return button functionality
        btnReturnLoan.setOnClickListener(v -> returnSelectedLoans());
    }

    private void searchLoans() {
        String loanNumber = etLoanNumber.getText().toString().trim();

        if (loanNumber.isEmpty()) {
            Toast.makeText(this, "Venligst indtast gyldigt lånenummer", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate finding user by loan number
        userLoans = findLoansByLoanNumber(Integer.parseInt(loanNumber));

        if (userLoans.isEmpty()) {
            Toast.makeText(this, "Ingen lån fundet for denne bruger", Toast.LENGTH_SHORT).show();
            return;
        }

        // Populate the loan list
        populateLoanList();
    }

    private List<LoanModel> findLoansByLoanNumber(int loanNumber) {
        // Fetch the user by loan number from the database
        UserModel user = dbHelper.getUserByLoanNumber(loanNumber);

        // If user is found, return their loans
        if (user != null) {
            return user.getLoans();
        } else {
            return new ArrayList<>(); // No loans found for this loan number
        }
    }

    private void populateLoanList() {
        loanListLayout.removeAllViews();
        loanCheckBoxes.clear();

        for (LoanModel loan : userLoans) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(loan.getItem().getType() + " - " + loan.getItem().getName());
            loanListLayout.addView(checkBox);
            loanCheckBoxes.add(checkBox);
        }

        tvLoanListHeader.setVisibility(View.VISIBLE);
        loanListLayout.setVisibility(View.VISIBLE);
        btnReturnLoan.setVisibility(View.VISIBLE);
    }

    private void returnSelectedLoans() {
        List<LoanModel> loansToReturn = new ArrayList<>();

        for (int i = 0; i < loanCheckBoxes.size(); i++) {
            if (loanCheckBoxes.get(i).isChecked()) {
                loansToReturn.add(userLoans.get(i));
            }
        }

        if (loansToReturn.isEmpty()) {
            Toast.makeText(this, "Venligst vælg mindst én genstand til aflevering", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remove selected loans from the database
        for (LoanModel loan : loansToReturn) {
            dbHelper.removeLoan(loan);
        }

        // Remove loans from the local list (for UI update)
        userLoans.removeAll(loansToReturn);

        // Update the loan list UI
        populateLoanList();

        Toast.makeText(this, "Valgte lån er blevet afleveret!", Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
