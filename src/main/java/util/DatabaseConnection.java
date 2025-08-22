package util;

import java.sql.*;

public class DatabaseConnection {

    private static Connection connection;

    // Database credentials and URL - update these as per your DB setup
    private static final String URL = "jdbc:mysql://localhost:3306/doer13";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    // Get a single shared connection instance
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Initialize the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    // Optional: method to close the connection if needed
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
