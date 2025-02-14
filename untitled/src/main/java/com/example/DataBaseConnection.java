package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    // Σταθερές για τα στοιχεία της βάσης δεδομένων
    private static final String URL = "jdbc:mysql://localhost:3306/event_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }

        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}