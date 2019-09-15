package ru.levelp;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

    public static void main(String[] args) throws Exception {
        ExecutorService createThread = Executors.newFixedThreadPool(2);

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

            createThread.submit(() -> {
                try {
                    chatRead(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            createThread.submit(() -> {
                try {
                    chatWrite(output, input, keyboardInput);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void chatWrite(Writer output, BufferedReader input, BufferedReader keyboardInput) throws IOException{
        while (true){
            output.write(keyboardInput.readLine() + "\n");
            output.flush();
        }
    }

    public static void chatRead(BufferedReader input) throws IOException{
        while (true) {
            System.out.println(input.readLine());
        }
    }
}
