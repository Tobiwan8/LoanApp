package com.example.loanapp.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonUtil {

    private static final String JSON_FILE_NAME = "users.json";

    // Read JSON file from assets folder
    public static JSONArray readJsonFromAssets(Context context) {
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(JSON_FILE_NAME)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            return new JSONArray(json.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONArray(); // Return empty array if file not found or invalid
        }
    }

    // Write JSON data to the app's internal storage
    public static void writeJsonToFile(Context context, JSONArray jsonArray) {
        try (FileOutputStream fos = context.openFileOutput(JSON_FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(jsonArray.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read JSON data from internal storage
    public static JSONArray readJsonFromFile(Context context) {
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.openFileInput(JSON_FILE_NAME)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            return new JSONArray(json.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONArray(); // Return empty array if file not found or invalid
        }
    }
}
