package ru.levelp.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    public static final int SERVER_PORT = 9994;
    public static final List<Socket> listUsers = new CopyOnWriteArrayList<>();
    public static final List<MessageListener> listeners = new CopyOnWriteArrayList<>();


    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(SERVER_PORT);
        ExecutorService exec = Executors.newFixedThreadPool(100);

        try {
            while (true) {
                Socket client = server.accept();
                listUsers.add(client);
                exec.submit(() -> {
                    try {
                        processClient(client);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } finally {
            exec.shutdown();
        }
    }

    public void registerMessageListener(MessageListener listener) {
        listeners.add(listener);
    }

    private static void processClient(Socket client) throws Exception {
        try (Writer output = new OutputStreamWriter(
                new BufferedOutputStream(
                        client.getOutputStream()))) {
            try (BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream()))) {
                output.write("Hello dear, enter your nickname:\n");
                output.flush();
                String nickname = input.readLine();

                output.write("Welcome to chat room: " + nickname + "\n");
                output.flush();

                output.write("You can to write message :\n");
                output.flush();

                UserNotificationListener userNotificationListener = new UserNotificationListener();
                listeners.add(userNotificationListener);

                while (true) {
                    String message = input.readLine();
                    for (MessageListener listener : listeners) {
                        listener.messageAdded(nickname,message);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Client disconnected.");
        } finally {
            client.close();
            listUsers.remove(client);
        }
    }
}



