package NEWSAVES;

import com.example.DataBaseConnection;

import java.sql.*;

public class NewEvent {

    // Constructor που δέχεται όλα τα πεδία
    public NewEvent(String name,String date,String time,String event_type,int capacity ) {


        String insertEventSQL = "INSERT INTO events (name,date,time,event_type,capacity) "
                + "VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertEventSQL)) {

            pstmt.setString(1, name);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            pstmt.setString(4, event_type);
            pstmt.setInt(5, capacity);

            // Εκτέλεση εντολής
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Event added successfully!");
            } else {
                System.out.println("Failed to add event.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while adding event: " + e.getMessage());
        }
    }

    // Constructor που δέχεται μόνο υποχρεωτικά πεδία
    public NewEvent(String name,String date) {
        this(name, date, null, null, -1);
    }
}
