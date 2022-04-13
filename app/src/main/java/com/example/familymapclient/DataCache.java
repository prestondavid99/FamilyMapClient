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
    private ArrayList<PolylineOptions> eventLines = new ArrayList<>();
    private ArrayList<PolylineOptions> spouseLines = new ArrayList<>();

    public void clearSpouseLines() {
        this.spouseLines.clear();
    }

    public void addSpouseLine(PolylineOptions p) {
        spouseLines.add(p);
    }

    public ArrayList<PolylineOptions> getSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(ArrayList<PolylineOptions> spouseLines) {
        this.spouseLines = spouseLines;
    }

    private ArrayList<PolylineOptions> familyLines = new ArrayList<>();

    private Person[] people;
    private Event[] events;
    private Map<Person, ArrayList<Event>> eventsMap = new HashMap<>();

    public Person getCurrPerson() {
        return currPerson;
    }

    public void setCurrPerson(Person currPerson) {
        this.currPerson = currPerson;
    }

    Person currPerson;

    private ArrayList<Event> personEvents = new ArrayList<>();
    private ArrayList<Person> personFamily = new ArrayList<>();

    public ArrayList<Event> getPersonEvents() {
        return personEvents;
    }

    public void addPersonEvent(Event e) {
        personEvents.add(e);
    }

    public void addPersonFamily(Person p) {
        personFamily.add(p);
    }

    public void setPersonEvents(ArrayList<Event> personEvents) {
        this.personEvents = personEvents;
    }

    public ArrayList<Person> getPersonFamily() {
        return personFamily;
    }

    public void setPersonFamily(ArrayList<Person> personFamily) {
        this.personFamily = personFamily;
    }

    public ArrayList<PolylineOptions> getEventLines() {
        return eventLines;
    }

    public void setEventLines(ArrayList<PolylineOptions> eventLines) {
        this.eventLines = eventLines;
    }

    public void clearEventLines() {
        this.eventLines.clear();
    }

    public void clearFamilyLines() {
        this.familyLines.clear();
    }

    public ArrayList<PolylineOptions> getFamilyLines() {
        return familyLines;
    }

    public void setFamilyLines(ArrayList<PolylineOptions> familyLines) {
        this.familyLines = familyLines;
    }

    public void addFamilyLine(PolylineOptions polylineOptions) {
        this.familyLines.add(polylineOptions);
    }

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
