package com.example.familymapclient;

import android.content.Context;
import android.media.metrics.Event;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.net.URL;
import java.util.List;

import model.Person;
import requestresult.EventResult;
import requestresult.LoginResult;
import requestresult.LoginRequest;
import requestresult.PersonResult;

public class DownloadTask implements Runnable {
    private final Handler messageHandler;
    private String hostServer;
    private String hostPort;
    private LoginRequest l;
    private Context context;

    public DownloadTask(Handler messageHandler, String hostSever, String hostPort, LoginRequest l, Context context) {
        this.messageHandler = messageHandler;
        this.hostServer = hostSever;
        this.hostPort = hostPort;
        this.l = l;
        this.context = context;
    }

    @Override
    public void run() {


        DataCache dataCache = DataCache.getInstance();
        ServerProxy sp = new ServerProxy(hostServer, hostPort);
        LoginResult loginResult = sp.login(l);

        if (loginResult.isSuccess()) {
            EventResult eventResult = sp.getEvents(loginResult.getAuthtoken());
            PersonResult personResult = sp.getPeople(loginResult.getAuthtoken());
            dataCache.setEvents(eventResult.getData());
            dataCache.setPeople(personResult.getData());

            Person person = findPerson(loginResult.getPersonID(), personResult.getData());

            CharSequence text = person.getFirstName() + " " + person.getLastName();

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        sendMessage(loginResult);
    }

    private void sendMessage(LoginResult result) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
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
