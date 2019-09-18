package ru.levelp;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.levelp.Utils.chatRead;
import static ru.levelp.Utils.chatWrite;


public class Client {

    public static void main(String[] args) throws IOException {
        ExecutorService exec = Executors.newFixedThreadPool(2);

        BufferedReader keyboardInput = new BufferedReader(
                new InputStreamReader(System.in));

        Socket connection = new Socket("localhost", Server.SERVER_PORT);
        try {
            Writer output = new OutputStreamWriter(
                    new BufferedOutputStream(
                            connection.getOutputStream()));
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            System.out.println(input.readLine());
            output.write(keyboardInput.readLine() + "\n");
            output.flush();

            exec.submit(() -> {
                try {
                    chatRead(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            exec.submit(() -> {
                try {
                    chatWrite(output, keyboardInput);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            exec.shutdown();
        }
    }


}
