package com.example.familymapclient;

import android.content.Context;
import android.media.metrics.Event;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import model.Person;
import requestresult.EventResult;
import requestresult.LoginRequest;
import requestresult.LoginResult;
import requestresult.PersonResult;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;


public class DownloadTask implements Runnable {
    private final Handler messageHandler;
    private String hostServer;
    private String hostPort;
    private LoginRequest l;
    private RegisterRequest r;

    public DownloadTask(Handler messageHandler, String hostSever, String hostPort, LoginRequest l) {
        this.messageHandler = messageHandler;
        this.hostServer = hostSever;
        this.hostPort = hostPort;
        this.l = l;
    }

    public DownloadTask(Handler messageHandler, String hostSever, String hostPort, RegisterRequest r) {
        this.messageHandler = messageHandler;
        this.hostServer = hostSever;
        this.hostPort = hostPort;
        this.r = r;
    }

    @Override
    public void run() {


        DataCache dataCache = DataCache.getInstance();
        ServerProxy sp = new ServerProxy(hostServer, hostPort);
        LoginResult loginResult = sp.login(l);
        RegisterResult registerResult = sp.register(r);
        String text;
        if (loginResult.isSuccess()) {
            EventResult eventResult = sp.getEvents(loginResult.getAuthtoken());
            PersonResult personResult = sp.getPeople(loginResult.getAuthtoken());
            dataCache.setEvents(eventResult.getData());
            dataCache.setPeople(personResult.getData());

            Person person = findPerson(loginResult.getPersonID(), personResult.getData());

            text = person.getFirstName() + " " + person.getLastName();
        } else {
            text = "Login Failed;";
        }

        if (registerResult.isSuccess()) {
            EventResult eventResult = sp.getEvents(registerResult.getAuthtoken());
            PersonResult personResult = sp.getPeople(registerResult.getAuthtoken());
            dataCache.setEvents(eventResult.getData());
            dataCache.setPeople(personResult.getData());

            Person person = findPerson(loginResult.getPersonID(), personResult.getData());

            text = person.getFirstName() + " " + person.getLastName();
        } else {
            text = "Register Failed;";
        }
        sendMessage(loginResult, text);
    }

    private void sendMessage(LoginResult result, String text) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString("firstLastName", text);
        messageBundle.putBoolean("Success", result.isSuccess());
        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }

    public Person findPerson(String personID, Person[] personArray) {
        for (Person p : personArray) {
            if (p.getPersonID().equals(personID)) {
                return p;
            }
        }
        return null;
    }
}
