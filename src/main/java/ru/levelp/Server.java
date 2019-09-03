package ru.levelp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int SERVER_PORT = 9994;

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(SERVER_PORT);
        System.out.println("Client connected");
        while (true) {
            Socket client = server.accept();
            new Thread(() -> {
                try {
                    processClient(client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
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
                    output.write("Enter a:\n");
                    output.flush();
                    double a = Double.parseDouble(input.readLine());

                    output.write("Enter b:\n");
                    output.flush();
                    double b = Double.parseDouble(input.readLine());

                    output.write("Result: " + (a + b) + "\n");
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

