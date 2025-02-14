package ACTIONSAVES;

import com.example.DataBaseConnection;

import java.sql.*;

public class BookTicket {

    public BookTicket(int customer_id,int event_id,String seat_type,int amount) {



        try (Connection conn = DataBaseConnection.getConnection()) {

            String selectTicketsSQL = "SELECT * FROM tickets WHERE event_id = ? AND seat_type = ? AND availability >= ?";

            try (PreparedStatement stmt = conn.prepareStatement(selectTicketsSQL)) {
                stmt.setInt(1, event_id);
                stmt.setString(2, seat_type);
                stmt.setInt(3, amount);

                ResultSet rsT = stmt.executeQuery();

                if(rsT.next()){
                    float ticketprice = rsT.getFloat("price");
                    String selectCostumerSQL = "SELECT C_C_Balance FROM customers WHERE id = ?";
                    try (PreparedStatement stmt1 = conn.prepareStatement(selectCostumerSQL)){

                        stmt1.setInt(1, customer_id);
                        ResultSet rsC = stmt1.executeQuery();

                        if(rsC.next()){

                            float CBalance = rsC.getFloat("C_C_Balance");

                            if(CBalance > amount*ticketprice){

                                String updateCustomerBalanceSQL = "UPDATE customers SET C_C_Balance = C_C_Balance - ? WHERE id = ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateCustomerBalanceSQL)) {
                                    updateStmt.setFloat(1, amount*ticketprice);
                                    updateStmt.setInt(2, customer_id);
                                    updateStmt.executeUpdate();
                                }


                                String updateTicketAvailabilitySQL = "UPDATE tickets SET availability = availability - ? WHERE event_id = ? AND seat_type = ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateTicketAvailabilitySQL)) {
                                    updateStmt.setInt(1, amount);
                                    updateStmt.setInt(2, event_id);
                                    updateStmt.setString(3, seat_type);
                                    updateStmt.executeUpdate();
                                }

                                System.out.println("Ticket booked successfully.");

                            }else{
                                System.out.println("Can't book the ticket: Not enough balance");
                            }


                        }
                        else{
                            System.out.println("Costumer not found");
                        }

                    }

                } else {
                    System.out.println("Not available ticket with event id " + event_id + " and seat type "+seat_type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }

    }

}
