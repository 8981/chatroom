package ru.levelp.Server;

import java.sql.*;

public class DataBaseNotificationListener implements DataBaseListener {
    @Override
    public void messageAddedToBase(String message) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:./bd")) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS Messages ( " +
                        " message VARCHAR(250)) ");

                statement.executeUpdate("INSERT INTO Messages (message) VALUES ('" + message + "')");

                statement.execute("SELECT  message FROM Messages");

            }
        }
    }
}
