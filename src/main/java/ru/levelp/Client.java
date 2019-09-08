package ru.levelp;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
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


            System.out.println(input.readLine());

        } finally {
            connection.close();
        }
    }
}
