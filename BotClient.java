package com.codegym.task.task30.task3008.client;

import com.codegym.task.task30.task3008.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BotClient extends Client {

    public class BotSocketThread extends Client.SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Hello, there. I'm a bot. I understand the following commands: date, day, month, year, time, hour, minutes, seconds.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
                ConsoleHelper.writeMessage(message);

                if (message.contains(": ")) {
                    int index = message.indexOf(": ");
                    String userName = message.substring(0, index);
                    String text = message.substring(index + 2);

                    Date date = Calendar.getInstance().getTime();
                    switch (text) {

                        case "date": {
                            sendTextMessage("Information for " + userName + ": " + new SimpleDateFormat("d.MM.YYYY").format(date));
                            break;
                        } case "day": {
                            sendTextMessage("Information for " + userName + ": " + new SimpleDateFormat("d").format(date));
                            break;
                        } case "month": {
                            sendTextMessage("Information for " + userName + ": " + new SimpleDateFormat("MMMM").format(date));
                            break;
                        } case "year": {
                            sendTextMessage("Information for " + userName + ": " + new SimpleDateFormat("YYYY").format(date));
                            break;
                        } case "time": {
                            sendTextMessage("Information for " + userName + ": " + new SimpleDateFormat("H:mm:ss").format(date));
                            break;
                        } case "hour": {
                            sendTextMessage("Information for " + userName + ": " + new SimpleDateFormat("H").format(date));
                            break;
                        } case "minutes": {
                            sendTextMessage("Information for " + userName + ": " + new SimpleDateFormat("m").format(date));
                            break;
                        } case "seconds": {
                            sendTextMessage("Information for " + userName + ": " + new SimpleDateFormat("s").format(date));
                            break;
                        }
                    }
                }
        }
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
