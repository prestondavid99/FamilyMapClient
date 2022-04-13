package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<Person> people;

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
        people = cache.getPersonFamily();

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setAdapter();
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENT_POSITION = 0;
        private static final int PERSON_POSITION = 1;

        private ArrayList<Event> events;
        private ArrayList<Person> people;


        ExpandableListAdapter(ArrayList<Event> events, ArrayList<Person> people) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return events.size();
                case PERSON_POSITION:
                    return people.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return getString(R.string.click_on_a_marker_to_see_event_details);
                case PERSON_POSITION:
                    return getString(R.string.first_name);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return events.get(childPosition);
                case PERSON_POSITION:
                    return people.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_POSITION:
                    titleView.setText(R.string.list_view_events);
                    break;
                case PERSON_POSITION:
                    titleView.setText(R.string.list_view_people);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_text_box, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSON_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_activity_text_boxes, parent, false);
                    initializeHikingTrailView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View eventItemView, final int childPosition) {
            TextView eventText = eventItemView.findViewById(R.id.event_text);
            resortNameView.setText(skiResorts.get(childPosition).getName());

            TextView resortLocationView = eventItemView.findViewById(R.id.skiResortLocation);
            resortLocationView.setText(skiResorts.get(childPosition).getLocation());

            eventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, getString(R.string.skiResortToastText, skiResorts.get(childPosition).getName()), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void initializeHikingTrailView(View hikingTrailItemView, final int childPosition) {
            TextView trailNameView = hikingTrailItemView.findViewById(R.id.hikingTrailTitle);
            trailNameView.setText(hikingTrails.get(childPosition).getName());

            TextView trailLocationView = hikingTrailItemView.findViewById(R.id.hikingTrailLocation);
            trailLocationView.setText(hikingTrails.get(childPosition).getLocation());

            TextView trailDifficulty = hikingTrailItemView.findViewById(R.id.hikingTrailDifficulty);
            trailDifficulty.setText(hikingTrails.get(childPosition).getDifficulty());

            hikingTrailItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, getString(R.string.hikingTrailToastText, hikingTrails.get(childPosition).getName()), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
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