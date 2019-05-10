package com.example.personalizedgreetings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> contentArrayList = new ArrayList<>();
    static ArrayList<String> titleArrayList = new ArrayList<>();
    static ArrayList<String> pictureIDArrayList = new ArrayList<>();

    static List<String> defaultTitleList = Arrays.asList("Birthday", "Anniversary", "Get Well",
            "Retirement", "Newborn Baby", "College Acceptance", "Graduation", "Got a Job",
            "Promotion at Work", "Funeral",  "Wedding", "Serious Illness",
            "Borrow Money Shamelessly", "Dog Gave Birth", "Sent to Jail", "Won a Lawsuit");

    static List<String> defaultContentList = Arrays.asList("Age is just a number, especially after AGE! In your case, the number is getting really big! Have an amazing birthday FIRST NAME!",
            "FIRST NAME, every anniversary makes me look back at our relationship and realize that I had the best twelve months of my life.",
            "Hey FIRST NAME, it feels horrible to hear that you are so sick. Please take care of yourself my dear. Get well soon.",
            "Hey FIRST NAME, you always brightened up our workplace! We will miss your smiling face, and hope that you'll visit us when you have time. Farewell partner!",
            "Behold! A baby miracle has fallen upon the LAST NAME home. Congratulations on your new child!",
            "OMG FIRST NAME!!! CONGRATS!!! You did it! You're officially a college student! It only took AGE years!",
            "It only seems like yesterday when you were headed off to college. Congrats to FIRST NAME LAST NAME for finally heading out to the real world!",
            "Congratulations FIRST NAME, what an achievement! Now you're ready to take off. Wishing you smooth and enjoyable transition into your new job!",
            "So glad to hear about your promotion. May your life always be filled with such good opportunities.",
            "I was deeply saddened to hear about FIRST NAME's passing. I offer my condolences to you and your family.",
            "Once Upon a Time is really here & now! Congrats on your wedding and best wishes for a happy marriage in the years ahead!",
            "Dear FIRST NAME! Please accept my heartfelt sympathy in this painful time as you are fighting with your sickeness. Take care and remember that we love you and care about you.",
            "How are you FIRST NAME? I know it's been a long time since we spoke, but I am in a financial crisis and could really use some help. If you could spare me some money, please let me know ASAP. Thanks.",
            "Yo FIRST NAME I heard your dog recently had puppies. Can I get one?",
            "Hey I just saw on TV that a AGE year old \"FIRST NAME LAST NAME\" was wanted and on the run, that's not you is it?",
            "Hey FIRST NAME I heard that you finally won that lawsuit! Hurray for justice! Hope your life returns to normal soon.");

    static List<String> defaultImageList = Arrays.asList("birthday", "anniversary", "getwell",
            "retirement", "baby", "college", "graduation", "job", "promotion", "funeral",
            "wedding", "illness", "money", "dog", "jail", "lawsuit");


    static ArrayAdapter contentArrayAdapter, titleArrayAdapter, pictureIDArrayAdapter;
    static ListView listView, contentFakeView, pictureFakeView;

    SharedPreferences sharedPreferences;

    static String firstName = "";
    static String lastName = "";
    static String age = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        listView = findViewById(R.id.listView);
        contentFakeView = findViewById(R.id.contentFakeView);
        pictureFakeView = findViewById(R.id.pictureFakeView);

        // load past data
        try {
            contentArrayList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("CONTENT", ObjectSerializer.serialize(new ArrayList<String>())));
            titleArrayList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("TITLE", ObjectSerializer.serialize(new ArrayList<String>())));
            pictureIDArrayList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("PICTURE", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e){
            e.printStackTrace();
        }


        if ( titleArrayList.size() == 0 ) {
            titleArrayList.add("Add a new place...");
            contentArrayList.add("");
            pictureIDArrayList.add("");

            for ( int i = 0; i < defaultContentList.size(); i++ ) {
                titleArrayList.add(defaultTitleList.get(i));
                contentArrayList.add(defaultContentList.get(i));
                pictureIDArrayList.add(defaultImageList.get(i));
            }
        }

        //Show the notes on listView
        titleArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, titleArrayList);
        contentArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, contentArrayList);
        pictureIDArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, pictureIDArrayList);

        listView.setAdapter(titleArrayAdapter);
        contentFakeView.setAdapter(contentArrayAdapter);
        pictureFakeView.setAdapter(pictureIDArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( position > 0 ) {
                    Intent intent = new Intent(getApplicationContext(), ViewGreeting.class );
                    intent.putExtra("title", titleArrayList.get(position));
                    intent.putExtra("content", contentArrayList.get(position));
                    intent.putExtra("picture", pictureIDArrayList.get(position));
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), AddGreeting.class );
                    startActivity(intent);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if ( position > 0 ) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this note?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            titleArrayList.remove(position);
                                            titleArrayAdapter.notifyDataSetChanged();
                                            contentArrayList.remove(position);
                                            contentArrayAdapter.notifyDataSetChanged();
                                            pictureIDArrayList.remove(position);
                                            pictureIDArrayAdapter.notifyDataSetChanged();

                                            try {
                                                sharedPreferences.edit().putString("TITLE", ObjectSerializer.serialize(MainActivity.titleArrayList)).apply();
                                                sharedPreferences.edit().putString("CONTENT", ObjectSerializer.serialize(MainActivity.contentArrayList)).apply();
                                                sharedPreferences.edit().putString("PICTURE", ObjectSerializer.serialize(MainActivity.pictureIDArrayList)).apply();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                            )
                            .setNegativeButton("NO", null)
                            .show();
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.customize) {
            Intent intent = new Intent(getApplicationContext(), PersonSettings.class);
            startActivity(intent);

            return true;
        }

        return false;
    }
}
