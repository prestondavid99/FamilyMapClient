package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    DataCache cache = DataCache.getInstance();

    private TextView firstName;
    private TextView lastName;
    private TextView gender;

    private ExpandableListView listEvents;
    private ArrayList<Event> events;
    private ExpandableListView listFamily;
    private ArrayList<Person> family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        getSupportActionBar().setTitle("Person Information");

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        gender = findViewById(R.id.gender);

        firstName.setText(cache.getCurrPerson().getFirstName());
        lastName.setText(cache.getCurrPerson().getLastName());
        gender.setText(cache.getCurrPerson().getGender().toUpperCase());

        events = cache.getEventsMap().get(cache.getCurrPerson());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}