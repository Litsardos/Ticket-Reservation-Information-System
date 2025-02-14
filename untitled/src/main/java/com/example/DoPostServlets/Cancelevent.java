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

public class Cancelevent extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("doPost");
        String eventIdStr = request.getParameter("reservationId");
        int eventId = Integer.parseInt(eventIdStr);

        try (Connection conn = DataBaseConnection.getConnection()){

            if(Action.Eventcancellation(eventId)){
                System.out.println("Event Canceled");
                response.sendRedirect("main_admin.html");
            }
            else{
                System.out.println("Couldn't cancel event:");
                response.sendRedirect("cancel_event.html");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            response.sendRedirect("main_admin.html");
        }
    }
}
