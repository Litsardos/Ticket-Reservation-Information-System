package com.example.DoPostServlets;


import com.example.DataBaseConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

public class Login_check_admin extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String Username = request.getParameter("username");
        String Password = request.getParameter("password");

        try (Connection conn = DataBaseConnection.getConnection()){

            String selectFSeatsSQL = "SELECT * FROM admins WHERE name = ? AND password = ?";

            try (PreparedStatement stmt = conn.prepareStatement(selectFSeatsSQL)) {

                stmt.setString(1, Username);
                stmt.setString(2, Password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Login Success");
                    Cookie userCookie = new Cookie("userId", rs.getString("id"));
                    Cookie user = new Cookie("name", rs.getString("name"));
                    userCookie.setMaxAge(60*60*24); // Η διάρκεια ζωής του cookie (π.χ. 1 ημέρα)
                    userCookie.setPath("/"); // Καθορίζουμε ότι το cookie είναι διαθέσιμο σε όλη την εφαρμογή
                    response.addCookie(userCookie);
                    user.setMaxAge(60*60*24); // Η διάρκεια ζωής του cookie (π.χ. 1 ημέρα)
                    user.setPath("/"); // Καθορίζουμε ότι το cookie είναι διαθέσιμο σε όλη την εφαρμογή
                    response.addCookie(user);
                    response.sendRedirect("main_admin.html");//change main_admin.html
                }else{
                    System.out.println("Wrong Username or Password.");
                    response.sendRedirect("login_page_admin.html?error=invalid");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }
}
