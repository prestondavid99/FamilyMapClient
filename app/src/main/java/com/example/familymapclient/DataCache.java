package com.example.familymapclient;

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import model.Event;
import model.Person;

public class DataCache {

    private static DataCache instance;
    private ArrayList<PolylineOptions> eventLines;
    private ArrayList<PolylineOptions> familyLines;

    public ArrayList<PolylineOptions> getEventLines() {
        return eventLines;
    }

    public void setEventLines(ArrayList<PolylineOptions> eventLines) {
        this.eventLines = eventLines;
    }

    public ArrayList<PolylineOptions> getFamilyLines() {
        return familyLines;
    }

    public void setFamilyLines(ArrayList<PolylineOptions> familyLines) {
        this.familyLines = familyLines;
    }

    private Person[] people;
    private Event[] events;
    private Map<Person, ArrayList<Event>> eventsMap = new HashMap<>();

    public Person[] getPeople() {
        return people;
    }

    public Map<Person, ArrayList<Event>> getEventsMap() {
        return eventsMap;
    }

    public Person findPerson(Person[] people, String personId) {
        for (Person p : people) {
            if (p.getPersonID().equals(personId)) {
                return p;
            }
        }
        return null;
    }

    public void setPeople(Person[] people) {
        this.people = people;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    private DataCache() {

    }

    public void FillCache() {
        for (Person p : people) {
            String currId = p.getPersonID();
            ArrayList<Event> eventsList = new ArrayList<>();
            for (Event e : events) {
                if (currId.equals(e.getPersonID())) {
                    eventsList.add(e);
                }
            }
            eventsMap.put(p, eventsList);
        }
    }
}
