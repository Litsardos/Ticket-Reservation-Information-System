package ACTIONSAVES;

import com.example.DataBaseConnection;

import java.sql.*;

public class Event_cancellation {

    public static void Eventcancellation(int id) {


        try (Connection conn = DataBaseConnection.getConnection()) {

            String selectEventSQL = "SELECT * FROM events WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(selectEventSQL)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {

                    String deleteBookingsSQL = "DELETE FROM bookings WHERE event_id = ?";
                    try (PreparedStatement deleteBookingsStmt = conn.prepareStatement(deleteBookingsSQL)) {
                        deleteBookingsStmt.setInt(1, id);  // Χρησιμοποιούμε το event_id για να βρούμε τις κρατήσεις του γεγονότος
                        deleteBookingsStmt.executeUpdate();
                    }


                    String deleteTicketsSQL = "DELETE FROM tickets WHERE event_id = ?";
                    try (PreparedStatement deleteTicketsStmt = conn.prepareStatement(deleteTicketsSQL)) {
                        deleteTicketsStmt.setInt(1, id);  // Χρησιμοποιούμε το event_id για να βρούμε τα εισιτήρια του γεγονότος
                        deleteTicketsStmt.executeUpdate();
                    }


                    String deleteEventSQL = "DELETE FROM events WHERE id = ?";
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteEventSQL)) {
                        deleteStmt.setInt(1, id);  // Χρησιμοποιούμε το id του γεγονότος
                        int rowsAffected = deleteStmt.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Event deleted successfully.");
                        } else {
                            System.out.println("Event deletion failed.");
                        }
                    }


                } else {
                    System.out.println("Event with ID " + id + " not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }


}
