package com.example.DoPostServlets;

import com.example.DataBaseConnection;
import com.example.New;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

public class NewServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        try (Connection conn = DataBaseConnection.getConnection()){

            String selectFSeatsSQL = "SELECT * FROM customers WHERE loginname = ? OR password = ? OR name = ? OR phonenumber = ? OR lastname = ? OR email = ? OR C_C_Num = ? ";

            try (PreparedStatement stmt = conn.prepareStatement(selectFSeatsSQL)) {

                stmt.setString(1, request.getParameter("username"));
                stmt.setString(2, request.getParameter("password"));
                stmt.setString(3, request.getParameter("firstname"));
                stmt.setString(4, request.getParameter("lastname"));
                stmt.setString(5, request.getParameter("email"));
                stmt.setString(6, request.getParameter("phone number"));
                stmt.setString(7, request.getParameter("card number"));
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("WRONG INPUT");
                    response.sendRedirect("register.html?ERROR");

                }else{
                    New.NewCustomer(
                            request.getParameter("username"),
                            request.getParameter("password"),
                            request.getParameter("firstname"),
                            request.getParameter("lastname"),
                            request.getParameter("email"),
                            request.getParameter("phone number"),
                            request.getParameter("card number"),
                            request.getParameter("CVV "),
                            request.getParameter("expiry date"),
                            Integer.parseInt(request.getParameter("Balance")
                            ));

                    response.sendRedirect("login_screen.html");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }



    }
}
