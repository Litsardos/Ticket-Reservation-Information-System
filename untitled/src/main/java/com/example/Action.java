package com.example;
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.sql.Connection; import java.sql.PreparedStatement; import java.sql.ResultSet; import java.sql.SQLException; import java.time.LocalDate; import java.time.format.DateTimeFormatter; import java.util.HashMap; import java.util.Map;

public class Action {

    public static String RandomDateGenerator(){

            LocalDate start = LocalDate.of(2020, 1, 1);
            LocalDate end = LocalDate.now();
            long randomDay = ThreadLocalRandom.current().nextLong(start.toEpochDay(), end.toEpochDay() + 1);
            LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
            return randomDate.toString();

    }

    private static String addDates(String date,int months) {
        if(date==null){
            System.out.println("date is null");
            return null;
        }

        String[] parts = date.split("-");

        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1])+months;
        if(month >=12){
            month -= 12;
            year++;
        }
        int day = Integer.parseInt(parts[2]);


        return year+"-"+month+"-"+day;

    }

    private static String rounddate(String date,int months) {
        if(date==null){
            System.out.println("date is null");
            return null;
        }

        String[] parts = date.split("-");

        int year = Integer.parseInt(parts[0]);
        int thismonth = Integer.parseInt(parts[1])/months*months+1;
        int day = 1;


        return year+"-"+thismonth+"-"+day;

    }

    private static float splitdates(String date) {
        if(date==null){
            System.out.println("date is null");
            return 0;
        }
        String[] parts = date.split("-");

        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);


        return (day + month * 32 + year * 500) / 1000.0f;

    }

    public static boolean Bookingcancellation(int customer_id,int event_id,String seat_type,int amount){

        try (Connection conn = DataBaseConnection.getConnection()) {

            String selectBookingsSQL = "SELECT * FROM bookings WHERE event_id = ? AND seat_type = ? AND customer_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(selectBookingsSQL)) {
                stmt.setInt(1, event_id);
                stmt.setString(2, seat_type);
                stmt.setInt(3, customer_id);

                ResultSet rsT = stmt.executeQuery();

                if(rsT.next()){

                    String ticketpriceSQL =  "SELECT price FROM tickets WHERE event_id = ? AND seat_type = ?";
                    try (PreparedStatement stmt1 = conn.prepareStatement(ticketpriceSQL)){
                        stmt1.setInt(1, event_id);
                        stmt1.setString(2, seat_type);
                        ResultSet rs = stmt1.executeQuery();
                        if(rs.next()){
                            float ticketprice = rs.getFloat("price");
                            String updateCustomerBalanceSQL = "UPDATE customers SET C_C_Balance = C_C_Balance + ? WHERE id = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateCustomerBalanceSQL)) {
                                updateStmt.setFloat(1, amount * ticketprice);
                                updateStmt.setInt(2, customer_id);
                                updateStmt.executeUpdate();
                            }

                            String updateTicketAvailabilitySQL = "UPDATE tickets SET availability = availability + ? WHERE event_id = ? AND seat_type = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateTicketAvailabilitySQL)) {
                                updateStmt.setInt(1, amount);
                                updateStmt.setInt(2, event_id);
                                updateStmt.setString(3, seat_type);
                                updateStmt.executeUpdate();
                            }

                            int ticketAmount = rsT.getInt("ticket_amount");
                            if (ticketAmount > amount) {
                                String updateBookingStatsSQL = "UPDATE bookings SET ticket_amount = ticket_amount - ?, payment_amount = payment_amount - (? * ?) WHERE event_id = ? AND seat_type = ? AND customer_id = ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateBookingStatsSQL)) {
                                    updateStmt.setInt(1, amount);
                                    updateStmt.setInt(2, amount);
                                    updateStmt.setFloat(3, ticketprice);
                                    updateStmt.setInt(4, event_id);
                                    updateStmt.setString(5, seat_type);
                                    updateStmt.setInt(6, customer_id);
                                    updateStmt.executeUpdate();
                                }
                            } else if (ticketAmount == amount) {
                                String deleteBookingSQL = "DELETE FROM bookings WHERE event_id = ? AND seat_type = ? AND customer_id = ?";
                                try (PreparedStatement deleteTicketsStmt = conn.prepareStatement(deleteBookingSQL)) {
                                    deleteTicketsStmt.setInt(1, event_id);
                                    deleteTicketsStmt.setString(2, seat_type);
                                    deleteTicketsStmt.setInt(3, customer_id);
                                    deleteTicketsStmt.executeUpdate();
                                }
                            } else {
                                System.out.println("Ticket amount is too big");
                                return false;
                            }
                            System.out.println("Ticket unbooked successfully.");
                            return true;
                        }else{
                            System.out.println("Didn't find ticket's price.");
                            return false;
                        }


                    }

                } else {
                    System.out.println("Not booked ticket by customer_id "+customer_id+ " with event id " + event_id + " and seat type "+seat_type);
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return false;
        }

    }

    public static boolean Eventcancellation(int id) {


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
                            return true;
                        } else {
                            System.out.println("Event deletion failed.");
                            return false;
                        }
                    }


                } else {
                    System.out.println("Event with ID " + id + " not found.");
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static String[] Findavailabletickets() {

        ArrayList<String> list = new ArrayList<>();

        try (Connection conn = DataBaseConnection.getConnection()){

            String selectTicketsSQL = "SELECT tickets.*, e.name,e.date  FROM tickets JOIN events e ON tickets.event_id = id WHERE availability > 0 ORDER BY  event_id ASC,FIELD(seat_type, 'VIP','Golden','Seated','GA')";

            try (PreparedStatement stmt = conn.prepareStatement(selectTicketsSQL)) {

                ResultSet rs = stmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("Available ticket not found.");
                } else {

                    while (rs.next()) {
                        list.add(rs.getString("name"));
                        list.add(rs.getString("event_id"));
                        list.add(rs.getString("date"));
                        list.add(rs.getString("seat_type"));
                        list.add(rs.getString("price"));
                        list.add(rs.getString("availability"));
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
        return list.toArray(new String[0]);

    }

    public static void BookTicket(int customer_id,int event_id,String seat_type,int amount) {


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

                                if(rsT.getInt("availability")==amount){
                                    String deleteTicketSQL = "DELETE FROM tickets WHERE event_id = ? AND seat_type = ?";
                                    try (PreparedStatement deleteTicketStmt = conn.prepareStatement(deleteTicketSQL)) {
                                        deleteTicketStmt.setInt(1, event_id);
                                        deleteTicketStmt.setString(2, seat_type);
                                        deleteTicketStmt.executeUpdate();
                                    }
                                }else{
                                    String updateTicketAvailabilitySQL = "UPDATE tickets SET availability = availability - ? WHERE event_id = ? AND seat_type = ?";
                                    try (PreparedStatement updateStmt = conn.prepareStatement(updateTicketAvailabilitySQL)) {
                                        updateStmt.setInt(1, amount);
                                        updateStmt.setInt(2, event_id);
                                        updateStmt.setString(3, seat_type);
                                        updateStmt.executeUpdate();
                                    }
                                }


                                New.NewBooking(customer_id,event_id,amount,seat_type);

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
                    System.out.println("Not enough available ticket with event id " + event_id + " and seat type "+seat_type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void ShowseatStatus(){

        try (Connection conn = DataBaseConnection.getConnection()){

            String selectFSeatsSQL = "SELECT * FROM tickets WHERE availability > 0 ORDER BY event_id ASC,FIELD(seat_type, 'VIP','Golden','Seated','GA')";

            try (PreparedStatement stmt = conn.prepareStatement(selectFSeatsSQL)) {

                ResultSet rs = stmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("Available tickets not found.");
                } else {
                    System.out.println("    AVAILABLE SEATS");
                    System.out.printf("%-10s %-20s%n", "Event ID", "Seat Type");
                    while (rs.next()) {
                        int eventId = rs.getInt("event_id");
                        String seatType = rs.getString("seat_type");
                        System.out.printf("%-10d %-20s%n", eventId, seatType);

                    }
                }
            }

            String selectBSeatsSQL = "SELECT * FROM bookings ORDER BY event_id ASC";

            try (PreparedStatement stmt = conn.prepareStatement(selectBSeatsSQL)) {

                ResultSet rs = stmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("Booked tickets not found.");
                } else {

                    System.out.println("\n    BOOKED SEATS");
                    System.out.printf("%-10s %-20s%n", "Event ID", "Seat Type");
                    while (rs.next()) {
                        int eventId = rs.getInt("event_id");
                        String seatType = rs.getString("seat_type");

                        System.out.printf("%-10d %-20s%n", eventId, seatType);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void ShowIncome() {

        try (Connection conn = DataBaseConnection.getConnection()) {


            String selectIncomeSQL = "SELECT e.name,e.id, b.payment_amount FROM events e LEFT JOIN bookings b ON e.id = b.event_id ORDER BY e.id ASC;";

            try (PreparedStatement stmt = conn.prepareStatement(selectIncomeSQL)) {

                ResultSet rs = stmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("No income data found.");
                } else {
                    int flag=-1;
                    System.out.println("\n    EVENT INCOME");
                    System.out.printf("%-10s %-20s %-15s%n", "Event ID", "Event Name", "Income");
                    float sum=0;
                    String namee=null,prevname=null;
                    while (rs.next()){
                        int eventId = rs.getInt("id");
                        namee = rs.getString("name");
                        if(flag==-1){
                            flag=eventId;
                        }
                        if(flag!=eventId){
                            System.out.printf("%-10d %-20s %-15f%n", flag, prevname, sum);
                            sum=0;
                        }
                        sum=sum+rs.getFloat("payment_amount");
                        prevname=rs.getString("name");
                        flag=eventId;
                    }
                    System.out.printf("%-10d %-20s %-15f%n", flag, namee, sum);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void ShowmostPopular() {
        try (Connection conn = DataBaseConnection.getConnection()) {

            // SQL ερώτημα για εμφάνιση event_id, event_name και total_tickets
            String selectIncomeSQL = "SELECT e.id AS event_id, e.name AS event_name,COALESCE(SUM(b.ticket_amount), 0) AS total_tickets FROM events e " +
                    "LEFT JOIN bookings b ON e.id = b.event_id " +
                    "GROUP BY e.id, e.name " +
                    "ORDER BY total_tickets DESC;";

            try (PreparedStatement stmt = conn.prepareStatement(selectIncomeSQL)) {

                ResultSet rs = stmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("No booking data found.");
                } else {
                    System.out.println("\n    EVENT POPULARITY");
                    System.out.printf("%-10s %-20s %-15s%n", "Event ID", "Event Name", "Total Tickets");
                    while (rs.next()) {
                        int eventId = rs.getInt("event_id");
                        String eventName = rs.getString("event_name");
                        int totalTickets = rs.getInt("total_tickets");

                        System.out.printf("%-10d %-20s %-15d%n", eventId, eventName, totalTickets);
                    }
                }

            } catch (SQLException e) {
                System.out.println("Error during SQL execution: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error during database connection: " + e.getMessage());
        }
    }

    public static void ShowMostIncome(String firstdate, String seconddate) {

        try (Connection conn = DataBaseConnection.getConnection()) {

            String selectIncomeSQL = "SELECT e.id AS event_id,e.name AS event_name,b.date,b.payment_amount FROM events e RIGHT JOIN bookings b ON e.id = b.event_id ORDER BY event_id ASC";

            try (PreparedStatement stmt = conn.prepareStatement(selectIncomeSQL)) {
                ResultSet rs = stmt.executeQuery();

                float start =splitdates(firstdate);
                float end = splitdates(seconddate);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No booking data found.");
                } else {

                    float checkdate ;

                    int flag = -1;
                    int winnerid=-1;
                    float sum = -1,winnersum=-1;
                    float prevsum = -1;
                    String prevname = null,winnername=null;
                    int eventId;
                    while (rs.next()) {
                        eventId = rs.getInt("event_id");
                        if (flag == -1){
                            winnerid=eventId;
                            winnername=rs.getString("event_name");
                            winnersum=rs.getFloat("payment_amount");
                            sum = 0;
                            flag = eventId;
                        }
                        if (flag != eventId) {
                            if (sum>prevsum) {
                                winnerid=flag;
                                winnersum = sum;
                                winnername = prevname;
                                prevsum = sum;
                            }
                            sum = 0;
                        }
                        checkdate = splitdates(rs.getString("date"));
                        if (checkdate >= start && checkdate <= end) {
                            sum = sum + rs.getFloat("payment_amount");
                        }
                        flag = eventId;
                        prevname = rs.getString("event_name");
                    }
                    if (sum>prevsum) {
                        winnerid=flag;
                        winnersum = sum;
                        winnername = prevname;
                    }
                    System.out.println("\n1EVENT WITH MOST INCOME ID " + winnerid+" WITH NAME "+winnername+" WITH INCOME "+winnersum);
                }


            } catch (SQLException e) {
                System.out.println("Error during SQL execution: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error during database connection: " + e.getMessage());
        }
    }

    public static void BookingsByPeriod(int months) {

        try (Connection conn = DataBaseConnection.getConnection()){
            String selectIncomeSQL = "SELECT e.id AS event_id,e.name AS event_name,b.date,b.payment_amount,e.name,b.seat_type FROM events e RIGHT JOIN bookings b ON e.id = b.event_id ORDER BY date ASC";

            try (PreparedStatement stmt = conn.prepareStatement(selectIncomeSQL)) {

                ResultSet rs = stmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("No booking data found.");
                } else {
                    int flag=-1;
                    String prevdate=null;
                    float endingdate=-1;
                    while (rs.next()){
                        if(flag==-1){
                            prevdate=rounddate(rs.getString("date"),months);
                            System.out.println("\n                                      "+prevdate+" => "+addDates(prevdate,months));
                            System.out.println("                    Event Name: "+rs.getString("name")+", Seat type: "+rs.getString("seat_type")+", Date: "+rs.getString("date"));
                            prevdate=addDates(prevdate,months);
                            endingdate=splitdates(addDates(prevdate,months));
                            flag=0;
                        }
                        else{
                            float date=splitdates(rs.getString("date"));
                            if(date<=endingdate){
                                System.out.println("                    Event Name: "+rs.getString("name")+", Seat type: "+rs.getString("seat_type")+", Date: "+rs.getString("date"));
                            }
                            else{
                                while(date>endingdate){
                                    System.out.println("\n                                      "+prevdate+" => "+addDates(prevdate,months));
                                    prevdate=addDates(prevdate,months);
                                    endingdate=splitdates(prevdate);
                                    if(date>endingdate){
                                        System.out.println("                            No Booking found this period.");
                                    }else{
                                        System.out.println("                Event Name: "+rs.getString("name")+", Seat type: "+rs.getString("seat_type")+", Date: "+rs.getString("date"));
                                    }
                                }
                            }
                        }
                    }

                }


            } catch (SQLException e) {
                System.out.println("Error during SQL execution: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error during database connection: " + e.getMessage());
        }
    }

    public static void ShowIncomeBytickets(String seat_type,boolean total) {
        try (Connection conn = DataBaseConnection.getConnection()) {

            String selectIncomeSQL = "SELECT e.name,e.id, b.payment_amount,b.seat_type FROM events e LEFT JOIN bookings b ON e.id = b.event_id WHERE b.seat_type=? ORDER BY b.event_id ASC;";

            try (PreparedStatement stmt = conn.prepareStatement(selectIncomeSQL)) {
                stmt.setString(1,seat_type);

                ResultSet rs = stmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("No income data found.");
                } else {
                    float sum=0;
                    if(total){
                        while (rs.next()) {
                            sum+=rs.getFloat("payment_amount");
                        }
                        System.out.println("\n\nTotal income by Seat type: "+seat_type+" in general: €"+sum);
                    }else{
                        String name=null,prevname=null;
                        int flag=-1;
                        System.out.println("\n\n      Total income by Seat type: "+seat_type+" per event");
                        while (rs.next()){
                            int eventId = rs.getInt("id");
                            name = rs.getString("name");
                            if(flag==-1){
                                flag=eventId;
                                prevname=name;
                            }
                            if(flag!=eventId){
                                System.out.println("Event id: "+flag+" Event name: "+prevname +" INCOME: €"+sum);
                                sum=0;
                            }
                            sum=sum+rs.getFloat("payment_amount");
                            prevname=rs.getString("name");
                            flag=eventId;
                        }
                        System.out.println("Event id: "+flag+" Event name: "+prevname +" INCOME: €"+sum);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }

}
