package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int PERSON_VIEW_TYPE = 0;
    private static final int EVENT_VIEW_TYPE = 1;

    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Person> people = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
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

    private class PersonEventAdapter extends RecyclerView.Adapter<PersonEventViewHolder> {
        private ArrayList<Event> events;
        private ArrayList<Person> people;

        PersonEventAdapter(ArrayList<Event> events, ArrayList<Person> people) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getItemViewType(int position) {
            return position < events.size() ? EVENT_VIEW_TYPE : PERSON_VIEW_TYPE;
        }

        @NonNull
        @Override
        public PersonEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == EVENT_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.event_text_box, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.person_activity_text_boxes, parent, true);
            }
            return new PersonEventViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonEventViewHolder holder, int position) {
            if (position < events.size()) {

            }
        }


        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class PersonEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView name;
        private TextView eventText;

        private int viewType;
        private Person person;
        private Event event;

        public PersonEventViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            this.name = null;
            this.eventText = null;
            this.image = null;

            itemView.setOnClickListener(this);

            if (viewType == PERSON_VIEW_TYPE) {
                Drawable maleIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(50);
                Drawable femaleIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(50);
                name = itemView.findViewById(R.id.person_name);
                name.setText(formatPersonText(person));
                eventText = null;
                image = itemView.findViewById(R.id.person_icon);
                if (person.getGender().equals("m")) {
                    image.setImageDrawable(maleIcon);
                } else {
                    image.setImageDrawable(femaleIcon);
                }
            } else {
                Drawable mapMarker = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_map_marker).
                        colorRes(R.color.black).sizeDp(50);
                name = itemView.findViewById(R.id.person_name_event);
                name.setText(formatPersonText(person));
                eventText = itemView.findViewById(R.id.event_text);
                eventText.setText(formatEventText(event));
                image = itemView.findViewById(R.id.event_icon);
                image.setImageDrawable(mapMarker);

            }
        }

        private void bind(Event event) {
            this.event = event;
            eventText.setText(formatEventText(event));
            name.setText(formatPersonText(person));
        }

        private void bind(Person person) {
            this.person = person;
            name.setText(formatPersonText(person));
        }



        @Override
        public void onClick(View v) {
            if(viewType == EVENT_VIEW_TYPE) {
                // This is were we could pass the skiResort to a ski resort detail activity

                Toast.makeText(SearchActivity.this, String.format("Enjoy skiing %s!",
                        event.getEventType()), Toast.LENGTH_SHORT).show();
            } else {
                // This is were we could pass the hikingTrail to a hiking trail detail activity

                Toast.makeText(SearchActivity.this, String.format("Enjoy hiking %s. It's %s.",
                        formatPersonText(person), Toast.LENGTH_SHORT);
            }
        }

        public String formatPersonText(Person p) {
            return p.getFirstName() + " " + p.getLastName();
        }

        public String formatEventText(Event e) {
            return e.getEventType().toUpperCase() + ": " + e.getCity() + ", " + e.getCountry() + " ("
                    + e.getYear() + ")";
        }
    }
}