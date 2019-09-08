package ru.levelp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static final int SERVER_PORT = 9994;
    private static final List<ServerSocket> serverSockets = Collections.synchronizedList(new ArrayList<>());
    private static final List<ServerSocket> userName = Collections.synchronizedList(new ArrayList<>());



    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(SERVER_PORT);
        System.out.println("Client connected");
        while (true) {
            Socket client = server.accept();
            new Thread(() -> {
                try {
                    processClient(client);
                    connectionServer(server);
                    chatRoom(server);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void connectionServer(ServerSocket someConnection) {
        synchronized (serverSockets) {
            if (!serverSockets.contains(someConnection)) {
                serverSockets.add(someConnection);
            }
            System.out.println("User connected");
        }
    }

    private static void chatRoom(ServerSocket someChat) {
        for (int i = 0; i < userName.size(); i++) {
            someChat = userName.get(i);
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

                    output.write(nickname + "\n");
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

