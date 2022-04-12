package com.example.familymapclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
    private DataCache dataCache = DataCache.getInstance();

    public boolean isWorking() {
        return working;
    }

    private boolean working;

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


        ServerProxy sp = new ServerProxy(hostServer, hostPort);
        LoginResult loginResult = sp.login(l);
        RegisterResult registerResult = sp.register(r);
        String text;

        // LOGIN
        if (loginResult != null) {
            if (loginResult.isSuccess()) {
                EventResult eventResult = sp.getEvents(loginResult.getAuthtoken());
                PersonResult personResult = sp.getPeople(loginResult.getAuthtoken());
                dataCache.setEvents(eventResult.getData());
                dataCache.setPeople(personResult.getData());
                dataCache.FillCache();
                working = true;

                Person person = findPerson(loginResult.getPersonID(), personResult.getData());

                text = person.getFirstName() + " " + person.getLastName();
            } else {
                text = "Login Failed;";
                working = false;
            }
            sendMessage(loginResult, text);
        }

        // REGISTER
        if (registerResult != null) {
            if (registerResult.isSuccess()) {
                EventResult eventResult = sp.getEvents(registerResult.getAuthtoken());
                PersonResult personResult = sp.getPeople(registerResult.getAuthtoken());
                dataCache.setEvents(eventResult.getData());
                dataCache.setPeople(personResult.getData());
                dataCache.FillCache();
                working = true;

                Person person = findPerson(registerResult.getPersonID(), personResult.getData());

                text = person.getFirstName() + " " + person.getLastName();
            } else {
                working = false;
                text = "Register Failed;";
            }
            sendMessage(registerResult, text);
        }

        if (registerResult == null && loginResult == null) {
            text = "Error";
            working = false;
            sendMessage(loginResult, text);
        }
    }

    private void sendMessage(LoginResult result, String text) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString("firstLastName", text);
        if (result == null) {
            messageBundle.putBoolean("Failure", false);
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        } else {
            messageBundle.putBoolean("Success", result.isSuccess());
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }

    }

    private void sendMessage(RegisterResult result, String text) {
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

    public DataCache getDataCache() {
        return dataCache;
    }
}
