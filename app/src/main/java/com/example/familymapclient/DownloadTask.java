package com.example.familymapclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.net.URL;

public class DownloadTask implements Runnable {
    private final Handler messageHandler;
    private final URL[] urls;

    public DownloadTask(Handler messageHandler, URL[] urls) {
        this.messageHandler = messageHandler;
        this.urls = urls;
    }

    @Override
    public void run() {
        // TODO : sendMessage(totalSize?);
    }

    private void sendMessage(long totalSize) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        // TODO : messageBundle.putLong(TOTAL_SIZE_KEY, totalSize);
        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
