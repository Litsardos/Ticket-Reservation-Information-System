package NEWSAVES;

import com.example.DataBaseConnection;

import java.sql.*;

public class NewTicket {

    // Constructor που δέχεται όλα τα πεδία
    public NewTicket(int event_id ,String seat_type,int price,int availability ) {


        String insertTicketSQL = "INSERT INTO tickets(event_id,seat_type,price,availability) "
                + "VALUES (?, ?, ?, ?);";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertTicketSQL)) {

            pstmt.setInt(1, event_id);
            pstmt.setString(2, seat_type);
            pstmt.setInt(3, price);
            pstmt.setInt(4, availability);

            // Εκτέλεση εντολής
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Ticket added successfully!");
            } else {
                System.out.println("Failed to add ticket.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while adding ticket: " + e.getMessage());
        }
    }

    // Constructor που δέχεται μόνο υποχρεωτικά πεδία
    public NewTicket(int event_id,String seat_type) {
        this(event_id, seat_type, -1, -1);
    }
}
