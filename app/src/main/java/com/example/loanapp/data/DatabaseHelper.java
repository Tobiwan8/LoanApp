package com.example.loanapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.loanapp.model.ItemModel;
import com.example.loanapp.model.LoanModel;
import com.example.loanapp.model.UserModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "loan_app.db";
    private static final int DATABASE_VERSION = 3;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_LOANS = "loans";

    // User table columns
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_LOAN_NUMBER = "loan_number";

    // Loan table columns
    private static final String COLUMN_LOAN_ID = "loan_id";
    private static final String COLUMN_ITEM_TYPE = "item_type";
    private static final String COLUMN_ITEM_NAME = "item_name";
    private static final String COLUMN_LOAN_DATE = "loan_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULL_NAME + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_LOAN_NUMBER + " INTEGER UNIQUE" + ")";

        String createLoansTable = "CREATE TABLE " + TABLE_LOANS + " (" +
                COLUMN_LOAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_ITEM_TYPE + " TEXT, " +
                COLUMN_ITEM_NAME + " TEXT, " +
                COLUMN_LOAN_DATE + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        db.execSQL(createUsersTable);
        db.execSQL(createLoansTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Add or update user
    public void addOrUpdateUser(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, user.getFullName());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_LOAN_NUMBER, user.getLoanNumber());

        // Check if user already exists
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_LOAN_NUMBER + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(user.getLoanNumber())});
        if (cursor != null && cursor.moveToFirst()) {
            // User exists, update
            db.update(TABLE_USERS, values, COLUMN_LOAN_NUMBER + " = ?", new String[]{String.valueOf(user.getLoanNumber())});
        } else {
            // User does not exist, insert
            db.insert(TABLE_USERS, null, values);
        }

        // Add loans
        if (!user.getLoans().isEmpty()) {
            LoanModel lastLoan = user.getLoans().get(user.getLoans().size() - 1);

            // Insert the last loan into the loans table
            ContentValues loanValues = new ContentValues();
            loanValues.put(COLUMN_USER_ID, user.getLoanNumber());
            loanValues.put(COLUMN_ITEM_TYPE, lastLoan.getItem().getType());
            loanValues.put(COLUMN_ITEM_NAME, lastLoan.getItem().getName());
            loanValues.put(COLUMN_LOAN_DATE, lastLoan.getLoanDate().getTime());
            db.insert(TABLE_LOANS, null, loanValues);
        }

        cursor.close();
        db.close();
    }

    // Get all users
    public List<UserModel> getAllUsers() {
        List<UserModel> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int loanNumber = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAN_NUMBER));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));

                UserModel user = new UserModel(loanNumber, fullName, phoneNumber, email);

                // Get loans for this user
                String loanSelectQuery = "SELECT * FROM " + TABLE_LOANS + " WHERE " + COLUMN_USER_ID + " = ?";
                Cursor loanCursor = db.rawQuery(loanSelectQuery, new String[]{String.valueOf(loanNumber)});
                if (loanCursor.moveToFirst()) {
                    do {
                        int loanId = loanCursor.getInt(loanCursor.getColumnIndexOrThrow(COLUMN_LOAN_ID));
                        String itemType = loanCursor.getString(loanCursor.getColumnIndexOrThrow(COLUMN_ITEM_TYPE));
                        String itemName = loanCursor.getString(loanCursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME));
                        long loanDate = loanCursor.getLong(loanCursor.getColumnIndexOrThrow(COLUMN_LOAN_DATE));

                        LoanModel loan = new LoanModel(loanId, new ItemModel(itemType, itemName), new Date(loanDate));
                        user.getLoans().add(loan);
                    } while (loanCursor.moveToNext());
                }
                loanCursor.close();

                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    public UserModel getUserByLoanNumber(int loanNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        UserModel user = null;

        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_LOAN_NUMBER + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(loanNumber)});

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve user data from cursor
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            int userLoanNumber = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAN_NUMBER));

            // Create a new UserModel object
            user = new UserModel(userLoanNumber, fullName, phoneNumber, email);

            // Retrieve and add loans for this user
            String loanSelectQuery = "SELECT * FROM " + TABLE_LOANS + " WHERE " + COLUMN_USER_ID + " = ?";
            Cursor loanCursor = db.rawQuery(loanSelectQuery, new String[]{String.valueOf(userLoanNumber)});

            if (loanCursor.moveToFirst()) {
                do {
                    int loanId = loanCursor.getInt(loanCursor.getColumnIndexOrThrow(COLUMN_LOAN_ID));
                    String itemType = loanCursor.getString(loanCursor.getColumnIndexOrThrow(COLUMN_ITEM_TYPE));
                    String itemName = loanCursor.getString(loanCursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME));
                    long loanDate = loanCursor.getLong(loanCursor.getColumnIndexOrThrow(COLUMN_LOAN_DATE));

                    LoanModel loan = new LoanModel(loanId, new ItemModel(itemType, itemName), new Date(loanDate));
                    user.getLoans().add(loan);
                } while (loanCursor.moveToNext());
            }
            loanCursor.close();
        }
        cursor.close();
        db.close();
        return user;
    }

    public void removeLoan(LoanModel loan) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Assuming LoanModel has a way to identify the loan (e.g., using loan ID or a unique field)
        // Remove the loan using loanId
        String whereClause = COLUMN_LOAN_ID + " = ?";
        String[] whereArgs = { String.valueOf(loan.getLoanId()) };

        db.delete(TABLE_LOANS, whereClause, whereArgs);

        db.close();
//        String whereClause = COLUMN_ITEM_TYPE + " = ? AND " + COLUMN_ITEM_NAME + " = ? AND " + COLUMN_LOAN_DATE + " = ?";
//        String[] whereArgs = {
//                loan.getItem().getType(),
//                loan.getItem().getName(),
//                String.valueOf(loan.getLoanDate().getTime())
//        };
//
//        // Delete the loan from the loans table
//        db.delete(TABLE_LOANS, whereClause, whereArgs);
//
//        db.close();
    }

}
