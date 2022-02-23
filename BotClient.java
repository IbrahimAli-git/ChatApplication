package com.codegym.task.task30.task3008.client;

import java.io.IOException;

public class BotClient extends Client {

    public class BotSocketThread extends Client.SocketThread {

    }


    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() throws IOException {
        int X = (int) (Math.random() * 100);
        String userName = "date_bot_" + X;
        return userName;
    }

    @Override
    public void run() {

    }

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }
}
