package com.example.personalizedgreetings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.io.IOException;

public class PersonSettings extends AppCompatActivity {
    EditText firstNameEditText, lastNameEditText, ageEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_settings);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        ageEditText = findViewById(R.id.ageEditText);

        firstNameEditText.setText(MainActivity.firstName);
        lastNameEditText.setText(MainActivity.lastName);
        ageEditText.setText(MainActivity.age);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String firstName = "" + firstNameEditText.getText();
        String lastName = "" + lastNameEditText.getText();
        String age = "" + ageEditText.getText();

        if (firstName.length() > 0)
            MainActivity.firstName = firstName;
        if (lastName.length() > 0)
            MainActivity.lastName = lastName;
        if (age.length() > 0)
            MainActivity.age = age;

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

