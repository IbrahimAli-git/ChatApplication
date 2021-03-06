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

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            String s = "";
            while (true){
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message response = connection.receive();

                if (response.getType() == MessageType.USER_NAME){
                    String data = response.getData();
                    if (!data.isEmpty() && data != null){
                        if (!connectionMap.containsKey(data)){
                            connectionMap.put(data, connection);
                            s = data;
                            connection.send(new Message(MessageType.NAME_ACCEPTED));
                            break;
                        }
                    }
                }
            }
            return s;
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (Map.Entry<String, Connection>  pair : connectionMap.entrySet()){
                String clientName = pair.getKey();

                if (!(clientName.equals(userName))){
                    connection.send(new Message(MessageType.USER_ADDED, clientName));
                }

            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message response = connection.receive();
                MessageType messageType = response.getType();
                String data = response.getData();

                if (messageType == MessageType.TEXT) {
                    String generatedMessage = userName + ": " + data;
                    sendBroadcastMessage(new Message(MessageType.TEXT, generatedMessage));
                } else {
                    ConsoleHelper.writeMessage("Error not text");
                }
            }

        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage("" + socket.getRemoteSocketAddress());

            Connection connection = null;
            String userName = "";

            try {
                connection = new Connection(socket);
                userName = serverHandshake(connection);



                serverHandshake(connection);

                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));

                notifyUsers(connection, userName);

                serverMainLoop(connection, userName);

                connectionMap.remove(userName);

                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));

            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage(e.getMessage());
            } finally {
                try {
                    if (connection != null){
                        connection.close();
                        ConsoleHelper.writeMessage("connection closed");
                    }
                } catch (IOException io){
                    ConsoleHelper.writeMessage(io.getMessage());
                }
            }
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



