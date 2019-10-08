package ru.levelp.Server;

import java.io.IOException;

public interface MessageListener {
    void messageAdded(String nickname, String message) throws IOException;
}
