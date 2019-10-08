package ru.levelp.Server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import static ru.levelp.Server.Server.listUsers;

public class UserNotificationListener implements MessageListener {

    @Override
    public void messageAdded(String nickname, String message) throws IOException {
        for (Socket UserNotificationListener : listUsers) {
            Writer userWrite = new OutputStreamWriter(
                    new BufferedOutputStream(UserNotificationListener.getOutputStream()));
            userWrite.write(nickname + ": " + message + "\n");
            userWrite.flush();
        }
    }
}
