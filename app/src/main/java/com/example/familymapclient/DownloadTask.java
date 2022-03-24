package com.example.familymapclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.net.URL;

import requestresult.LoginResult;
import requestresult.LoginRequest;

public class DownloadTask implements Runnable {
    private final Handler messageHandler;
    private String hostServer;
    private String hostPort;
    private LoginRequest l;

    public DownloadTask(Handler messageHandler, String hostSever, String hostPort, LoginRequest l) {
        this.messageHandler = messageHandler;
        this.hostServer = hostSever;
        this.hostPort = hostPort;
        this.l = l;
    }

    @Override
    public void run() {
        ServerProxy sp = new ServerProxy(hostServer, hostPort);
        LoginResult loginResult = sp.login(l);
        sendMessage(loginResult);
    }

    private void sendMessage(LoginResult result) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean("Success", result.isSuccess());
        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
