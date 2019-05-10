package com.example.personalizedgreetings;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ViewGreeting extends AppCompatActivity {

    int whatToDo;
    ImageView imageView;
    TextView titleTextView, contentTextView;
    Button emailButton, textButton;

    String imageSrc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_greeting);

        Intent intent = getIntent();

        imageView = findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        emailButton = findViewById(R.id.emailButton);
        textButton = findViewById(R.id.textButton);

        String title = "" + intent.getStringExtra("title");
        String content = "" + intent.getStringExtra("content");

        if (MainActivity.firstName.length() > 0) {
            content = content.replace("FIRST NAME", MainActivity.firstName);
            content = content.replace("LAST NAME", MainActivity.lastName);
            content = content.replace("AGE", MainActivity.age);
        }

        titleTextView.setText(title);
        contentTextView.setText(content);
        imageSrc = intent.getStringExtra("picture");

        imageView.setImageResource(getResources().getIdentifier(imageSrc, "drawable", getPackageName()));

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, titleTextView.getText());
                i.putExtra(Intent.EXTRA_TEXT, contentTextView.getText());
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.setType("image/jpeg");

                Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + imageSrc);
                i.putExtra(Intent.EXTRA_STREAM, imageUri);

                startActivity(i);
            }
        });

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, contentTextView.getText());
                startActivity(i);
            }
        });
    }




}
