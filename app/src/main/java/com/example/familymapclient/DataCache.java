package com.example.familymapclient;

import model.Event;
import model.Person;

public class DataCache {

    private static DataCache instance;
    private Person[] people;
    private Event[] events;

    public Person[] getPeople() {
        return people;
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
}
