package com.example;


import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class MyServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {



        try (Connection conn = DataBaseConnection.getConnection();
            Statement stmt = conn.createStatement()){

            stmt.executeUpdate("DROP DATABASE IF EXISTS event_management;");

            stmt.executeUpdate("CREATE DATABASE event_management;");

            stmt.executeUpdate("USE event_management;");

            // Δημιουργία των πινάκων
            String createEventsTableSQL = "CREATE TABLE IF NOT EXISTS events ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "date VARCHAR(10) NOT NULL, "
                    + "time VARCHAR(10), "
                    + "event_type VARCHAR(100), "
                    + "capacity INT );";
            stmt.executeUpdate(createEventsTableSQL);

            String createAdmins = "CREATE TABLE IF NOT EXISTS admins ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "password VARCHAR(255) NOT NULL);";
            stmt.executeUpdate(createAdmins);

            String createCustomersTableSQL = "CREATE TABLE IF NOT EXISTS customers ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "loginname VARCHAR(255) NOT NULL, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "phonenumber VARCHAR(10), "
                    + "lastname VARCHAR(255), "
                    + "email VARCHAR(255), "
                    + "C_C_Num VARCHAR(16), "
                    + "C_C_CVV VARCHAR(3), "
                    + "C_C_Date VARCHAR(5), "
                    + "C_C_Balance INT , "
                    + "UNIQUE(loginname), "
                    + "UNIQUE(email) "
                    + ");";
            stmt.executeUpdate(createCustomersTableSQL);

            String createTicketsTableSQL = "CREATE TABLE IF NOT EXISTS tickets ("
                    + "event_id INT NOT NULL, "
                    + "seat_type VARCHAR(10) NOT NULL, "
                    + "price Float , "
                    + "availability INT , "
                    + "FOREIGN KEY (event_id) REFERENCES events(id));";
            stmt.executeUpdate(createTicketsTableSQL);


            String createBookingsTableSQL = "CREATE TABLE IF NOT EXISTS bookings ("
                    + "customer_id INT NOT NULL, "
                    + "event_id INT NOT NULL, "
                    + "ticket_amount INT, "
                    + "seat_type VARCHAR(10), "
                    + "date VARCHAR(10), "
                    + "payment_amount Float, "
                    + "FOREIGN KEY (customer_id) REFERENCES customers(id), "
                    + "FOREIGN KEY (event_id) REFERENCES events(id));";
            stmt.executeUpdate(createBookingsTableSQL);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        New.NewCustomer("giorgos","1","giorgos");
        New.NewCustomer("nikos","12","nikos");
        New.NewCustomer("giannhs","123","giannhs");
        New.NewCustomer("aggelos","1234","aggelos");
        New.NewAdmin("lits","2");
        New.NewEvent("toquel","2011/12/24");
        New.NewEvent("snik","2014/1/25");
        New.NewEvent("ivan greco","2020/1/25");
        New.NewEvent("lila","2013/2/25");
        New.NewEvent("50cent","2025/5/25");

        New.NewTicket(1, "VIP", 80, 2);
        New.NewTicket(2, "VIP", 200, 3);
        New.NewTicket(3, "VIP", 90, 1);
        New.NewTicket(2, "Golden", 60, 5);
        New.NewTicket(3, "GA", 40, 10);
        New.NewTicket(4, "Seated", 60, 4);
        New.NewTicket(3, "Golden", 50, 3);
        New.NewTicket(2, "GA", 35, 8);
        New.NewTicket(3, "Seated", 60, 6);
        New.NewTicket(4, "Golden", 70, 5);
        New.NewTicket(5, "Golden", 100, 3);

        New.NewBooking(1,2,3,"VIP");
        New.NewBooking(2,1,2,"VIP");
        New.NewBooking(3,3,3,"Seated");
        New.NewBooking(4,4,1,"Golden");


        Action.Eventcancellation(1);

        Action.Findavailabletickets();

        Action.BookTicket(1,2,"Golden",2);

        Action.Bookingcancellation(1,2,"Golden",1);

        Action.ShowseatStatus();

        Action.ShowIncome();

        Action.ShowmostPopular();

        Action.ShowMostIncome("2010-10-30","2025-12-25");

        Action.BookTicket(4,4,"Golden",5);
        Action.BookTicket(4,4,"Seated",2);
        Action.BookTicket(2,3,"GA",2);
        Action.BookTicket(3,2,"Golden",2);
        Action.BookTicket(1,5,"Golden",2);

        Action.ShowIncome();

        Action.ShowMostIncome("2015-10-30","2025-12-25");

        Action.BookingsByPeriod(3);

        Action.ShowIncomeBytickets("Golden",true);
        Action.ShowIncomeBytickets("Golden",false);

    }
}
