package ACTIONSAVES;

import com.example.DataBaseConnection;

import java.sql.*;

public class Findavailabletickets {

    public Findavailabletickets() {


        try (Connection conn = DataBaseConnection.getConnection()){

            String selectTicketsSQL = "SELECT * FROM tickets WHERE availability > 0 ORDER BY  event_id ASC,FIELD(seat_type, 'GA', 'Seated', 'Golden', 'VIP')";

            try (PreparedStatement stmt = conn.prepareStatement(selectTicketsSQL)) {

                ResultSet rs = stmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("Available ticket not found.");
                } else {
                    while (rs.next()) {
                        int eventId = rs.getInt("event_id");
                        String seatType = rs.getString("seat_type");

                        System.out.println("Event ID: " + eventId + ", Seat Type: " + seatType);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }

    }

}
