package NEWSAVES;

import com.example.DataBaseConnection;

import java.sql.*;

public class NewBooking {

    // Constructor που δέχεται όλα τα πεδία
    public NewBooking(int customer_id ,int event_id,int ticket_count,String date,int payment_amount ) {

        String insertBookingSQL = "INSERT INTO bookings(customer_id,event_id,ticket_count,date,payment_amount) "
                + "VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = DataBaseConnection.getConnection();

             PreparedStatement pstmt = conn.prepareStatement(insertBookingSQL)) {

            pstmt.setInt(1, customer_id);
            pstmt.setInt(2, event_id);
            pstmt.setInt(3, ticket_count);
            pstmt.setString(4, date);
            pstmt.setInt(5, payment_amount);

            // Εκτέλεση εντολής
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Booking added successfully!");
            } else {
                System.out.println("Failed to add booking.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while adding booking: " + e.getMessage());
        }
    }

    // Constructor που δέχεται μόνο υποχρεωτικά πεδία
    public NewBooking(int customer_id,int event_id) {
        this(customer_id, event_id, -1,null, -1);
    }
}
