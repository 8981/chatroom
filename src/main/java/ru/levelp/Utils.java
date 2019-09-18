package ru.levelp;

import java.io.*;
import java.net.Socket;

import static ru.levelp.Server.listUsers;

public class Utils {
    public static void processClient(Socket client) throws IOException {
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

                    while (true) {
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
        } catch (Exception e) {
            System.out.println("Client disconnected.");
        } finally {
            client.close();
            listUsers.remove(client);
        }
    }

    public static void chatWrite(Writer output, BufferedReader keyboardInput) throws IOException {
        while (true) {
            output.write(keyboardInput.readLine() + "\n");
            output.flush();
        }
    }

    public static void chatRead(BufferedReader input) throws IOException {
        while (true) {
            System.out.println(input.readLine());
        }
    }
}
