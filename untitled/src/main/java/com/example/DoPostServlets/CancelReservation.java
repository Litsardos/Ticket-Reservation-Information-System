package com.example.DoPostServlets;


import NEWSAVES.NewCustomer;
import com.example.Action;
import com.example.DataBaseConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CancelReservation")
public class CancelReservation extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String eventIdStr = request.getParameter("reservationId");
        String seatType = request.getParameter("seatType");
        String amount = request.getParameter("amount");

        Cookie[] cookies = request.getCookies();
        String userId = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    userId = cookie.getValue();
                }
            }
        } else {
            System.out.println("No cookies found!");
        }
        if (userId == null) {
            return;
        }
        try (Connection conn = DataBaseConnection.getConnection()){

            if(Action.Bookingcancellation(Integer.parseInt(userId),Integer.parseInt(eventIdStr),seatType,Integer.parseInt(amount))){
                System.out.println("Booking Canceled");
                response.sendRedirect("main.html");
            }
            else{
                System.out.println("Couldn't cancel booking:");
                response.sendRedirect("cancel_reservation.html");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }
}
