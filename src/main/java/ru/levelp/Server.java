package ru.levelp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int SERVER_PORT = 9994;
    private static final List<ServerSocket> serverSockets = Collections.synchronizedList(new ArrayList<>());
    private static final List<Socket> connections = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(SERVER_PORT);
        ExecutorService exec = Executors.newFixedThreadPool(100);

        try {
            while (true) {
                Socket client = server.accept();
                exec.submit(() -> {
                    try {
                        processClient(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    connectionServer(server);
                    try {
                        messageClient(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } finally {
            exec.shutdown();
        }
    }

    private static void connectionServer(ServerSocket someConnection) {
        synchronized (serverSockets) {
            if (!serverSockets.contains(someConnection)) {
                serverSockets.add(someConnection);
            } else {
                System.out.println("User connection");
            }
        }
    }

    private static void messageClient(Socket someMessage) throws IOException {
        for (int i = 0; i < connections.size(); i++) {
            try {
                try (Writer output = new OutputStreamWriter(
                        new BufferedOutputStream(
                                someMessage.getOutputStream()))) {
                    try (BufferedReader input = new BufferedReader(
                            new InputStreamReader(
                                    someMessage.getInputStream()))) {
                        output.write("You can write message:\n");
                        output.flush();
                        String message = input.readLine();

                        output.write("Message: " + message + "\n");
                        output.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                someMessage.close();

                System.out.println(connections.get(i));
            }
        }
    }

    private static void processClient(Socket client) throws IOException {
        try {
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

                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}

