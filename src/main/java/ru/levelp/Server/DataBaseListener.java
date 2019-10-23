package ru.levelp.Server;

import java.sql.SQLException;

public interface DataBaseListener {
    void messageAddedToBase(String message) throws SQLException;
}
