package com.example;

import java.sql.*;
import java.time.LocalDate;

public class New {

    public static void NewCustomer(String loginname, String password, String name, String lastname, String email,String phonenumber,
                                   String creditCardNumber, String creditCardCVV, String creditCardDate,int balance) {


        String insertCustomerSQL = "INSERT INTO customers (loginname, password, name, lastname, email,phonenumber ,C_C_Num, C_C_CVV, C_C_Date,C_C_Balance) "
                + "VALUES (?, ?,?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn =DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertCustomerSQL)) {

            // Ορισμός παραμέτρων στη SQL
            pstmt.setString(1, loginname);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, lastname); // Δέχεται null εάν δεν δοθεί
            pstmt.setString(5, email);
            pstmt.setString(6, phonenumber);
            pstmt.setString(7, creditCardNumber); // Δέχεται null εάν δεν δοθεί
            pstmt.setString(8, creditCardCVV);    // Δέχεται null εάν δεν δοθεί
            pstmt.setString(9, creditCardDate);   // Δέχεται null εάν δεν δοθεί
            pstmt.setInt(10, balance);

            // Εκτέλεση εντολής
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Customer added successfully!");
            } else {
                System.out.println("Failed to add customer.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while adding customer: " + e.getMessage());
        }
    }


    public static void NewEvent(String name,String date,String time,String event_type,int capacity ) {

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


    public static void NewTicket(int event_id ,String seat_type,float price,int availability ) {

        String insertTicketSQL = "INSERT INTO tickets(event_id,seat_type,price,availability) "
                + "VALUES (?, ?, ?, ?);";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertTicketSQL)) {

            pstmt.setInt(1, event_id);
            pstmt.setString(2, seat_type);
            pstmt.setFloat(3, price);
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


    public static void NewBooking(int customer_id, int event_id, int ticket_amount, String seat_type) {
        // Ερώτημα για να βρεις την τιμή του εισιτηρίου από τον τύπο θέσης
        String getTicketPriceSQL = "SELECT price FROM tickets WHERE seat_type = ?";

        // Ερώτημα για να κάνεις την εισαγωγή στην βάση
        String insertBookingSQL = "INSERT INTO bookings(customer_id, event_id, ticket_amount, seat_type, date, payment_amount) "
                + "VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection conn = DataBaseConnection.getConnection()) {
            // Βήμα 1: Ανάκτηση της τιμής του εισιτηρίου για το seat_type
            float ticketPrice = 0;
            try (PreparedStatement getPriceStmt = conn.prepareStatement(getTicketPriceSQL)) {
                getPriceStmt.setString(1, seat_type);
                ResultSet rs = getPriceStmt.executeQuery();

                if (rs.next()) {
                    ticketPrice = rs.getFloat("price");  // Παίρνουμε την τιμή του εισιτηρίου
                } else {
                    System.out.println("Seat type not found.");
                    return;  // Εάν δεν βρεθεί ο τύπος θέσης, σταματάμε την εκτέλεση
                }
            }

            // Βήμα 2: Υπολογισμός του payment_amount
            float paymentAmount = ticketPrice * ticket_amount;

            // Βήμα 3: Εισαγωγή της κράτησης στη βάση δεδομένων μαζί με το υπολογισμένο payment_amount
            try (PreparedStatement pstmt = conn.prepareStatement(insertBookingSQL)) {
                pstmt.setInt(1, customer_id);
                pstmt.setInt(2, event_id);
                pstmt.setInt(3, ticket_amount);
                pstmt.setString(4, seat_type);
                pstmt.setString(5, Action.RandomDateGenerator());
                //pstmt.setString(5, LocalDate.now().toString());
                pstmt.setFloat(6, paymentAmount);  // Βάζουμε το υπολογισμένο payment_amount

                // Εκτέλεση της εντολής
                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Booking added successfully!");
                } else {
                    System.out.println("Failed to add booking.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while adding booking: " + e.getMessage());
        }
    }

    public static void NewAdmin(String name, String password) {


        String insertCustomerSQL = "INSERT INTO admins (name ,password) "
                + "VALUES (?,?);";

        try (Connection conn =DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertCustomerSQL)) {

            // Ορισμός παραμέτρων στη SQL
            pstmt.setString(1, name);
            pstmt.setString(2, password);


            // Εκτέλεση εντολής
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Customer added successfully!");
            } else {
                System.out.println("Failed to add customer.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while adding customer: " + e.getMessage());
        }
    }

    public static void NewCustomer(String loginname, String password, String name) {
        NewCustomer(loginname, password, name, null,null, null, null, null, null,5000);
    }
    public static void NewEvent(String name,String date) {
        NewEvent(name, date, null, null, -1);
    }
    public static void NewTicket(int event_id,String seat_type) {
        NewTicket(event_id, seat_type, -1, -1);
    }
    public static void NewBooking(int customer_id,int event_id) {
        NewBooking(customer_id, event_id, -1,null);
    }
}
