package com.example.personalizedgreetings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.io.IOException;

public class AddGreeting extends AppCompatActivity {
    EditText contentEditText, titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_greeting);

        contentEditText = findViewById(R.id.contentEditText);
        titleEditText = findViewById(R.id.titleEditText);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();




        String title = "" + titleEditText.getText();
        String content = "" + contentEditText.getText();

        if (title.length() == 0)
            title = "No title";
        if (content.length() == 0)
            content = "No content";

        MainActivity.titleArrayList.add(title);
        MainActivity.contentArrayList.add(content);
        MainActivity.pictureIDArrayList.add("");

        MainActivity.titleArrayAdapter.notifyDataSetChanged();
        MainActivity.contentArrayAdapter.notifyDataSetChanged();
        MainActivity.pictureIDArrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        try {
            sharedPreferences.edit().putString("TITLE", ObjectSerializer.serialize(MainActivity.titleArrayList)).apply();
            sharedPreferences.edit().putString("CONTENT", ObjectSerializer.serialize(MainActivity.contentArrayList)).apply();
            sharedPreferences.edit().putString("PICTURE", ObjectSerializer.serialize(MainActivity.pictureIDArrayList)).apply();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
