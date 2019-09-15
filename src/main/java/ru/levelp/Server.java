package ru.levelp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int SERVER_PORT = 9994;
    public static final ArrayList<Socket> listUsers = new ArrayList<>();

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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } finally {
            exec.shutdown();
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

                    output.write("You can to write message :\n");
                    output.flush();


                    while (true){
                        String message = input.readLine();

                        for (Socket user : listUsers) {
                            Writer userWrite = new OutputStreamWriter(
                                    new BufferedOutputStream(
                                            user.getOutputStream()));

                            userWrite.write(nickname + " : " + message + "\n");
                            userWrite.flush();
                        }
                    }
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

