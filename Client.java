package com.codegym.task.task30.task3008.client;

import com.codegym.task.task30.task3008.Connection;
import com.codegym.task.task30.task3008.ConsoleHelper;
import com.codegym.task.task30.task3008.Message;
import com.codegym.task.task30.task3008.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;


    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }


    public class SocketThread extends Thread {
        protected void processIncomingMessage(String message){
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName){
            ConsoleHelper.writeMessage(userName + " has joined the chat.");
        }

        protected void informAboutDeletingNewUser(String userName){
            ConsoleHelper.writeMessage(userName + " has left the chat.");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected){
            Client.this.clientConnected = clientConnected;

            synchronized (Client.this){
                Client.this.notify();
            }
        }
    }


    public void run() {
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();

        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("An error occurred while running the client.");
                return;
            }
        }

        if (clientConnected) ConsoleHelper.writeMessage("A connection has been established. To exit, enter 'exit'.");
        else ConsoleHelper.writeMessage("An error occurred while running the client.");

        while (clientConnected){
            String message = ConsoleHelper.readString();

            if (message.equals("exit")){
                break;
            }

            if (shouldSendTextFromConsole()){
                sendTextMessage(message);
            }
        }
    }

    protected String getServerAddress() throws IOException {
        ConsoleHelper.writeMessage("Please enter the server address");
        return ConsoleHelper.readString();
    }

    protected int getServerPort() throws IOException {
        ConsoleHelper.writeMessage("Please enter server port");
        return ConsoleHelper.readInt();

    }

    protected String getUserName() throws IOException {
        ConsoleHelper.writeMessage("Please enter username");
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole(){
        return true;
    }

    protected SocketThread getSocketThread(){
        return new SocketThread();
    }

    protected void sendTextMessage(String text){
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            clientConnected = false;
        }
    }
}
