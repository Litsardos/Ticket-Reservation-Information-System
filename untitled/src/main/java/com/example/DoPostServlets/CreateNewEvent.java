package com.example.DoPostServlets;

import com.example.DataBaseConnection;
import com.example.New;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CreateNewEvent extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventName = request.getParameter("eventName");
        String eventDate = request.getParameter("eventDate");
        String eventLocation = request.getParameter("eventLocation");
        String eventTime = request.getParameter("eventTime");
        String eventType = request.getParameter("eventType");
        String eventCapacity = request.getParameter("eventCapacity");

        if(eventName == null || eventName.isEmpty() || eventDate == null || eventDate.isEmpty() || eventLocation == null || eventLocation.isEmpty() || eventTime == null || eventTime.isEmpty() || eventType == null || eventType.isEmpty() || eventCapacity == null || eventCapacity.isEmpty()) {
            System.out.println("Event name or event date is empty");
        }

        try (Connection conn = DataBaseConnection.getConnection()){

            String selectFSeatsSQL = "SELECT * FROM events WHERE name = ?";

            try (PreparedStatement stmt = conn.prepareStatement(selectFSeatsSQL)) {

                stmt.setString(1, eventName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Event name already exists");
                }else{
                    New.NewEvent(eventName,eventDate , eventTime ,eventType, Integer.parseInt(eventCapacity) );
                }
                response.sendRedirect("main_admin.html");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }

    }
}
