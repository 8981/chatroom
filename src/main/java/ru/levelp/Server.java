package ru.levelp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.levelp.Utils.processClient;

public class Server {
    public static final int SERVER_PORT = 9994;
    public static final List<Socket> listUsers = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
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


}
