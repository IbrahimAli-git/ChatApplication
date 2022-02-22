package com.codegym.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    private static class Handler extends Thread {
        private Socket socket;


        public Handler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {

        }
    }

    public static void sendBroadcastMessage(Message message){
        for (Map.Entry<String, Connection> pair : connectionMap.entrySet()){
            Connection con = pair.getValue();

            try {
                con.send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Could not send message");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ConsoleHelper.writeMessage("Please enter port number");
        ServerSocket serverSocket = new ServerSocket(ConsoleHelper.readInt());
        ConsoleHelper.writeMessage("Server is running");
        Socket socket = null;


            while (true) {
                try {
                    socket = serverSocket.accept();
                    new Handler(socket).start();

                } catch (Exception ioException) {
                    ConsoleHelper.writeMessage(ioException.getMessage());

                    serverSocket.close();
                    break;
                }
            }
    }
}
