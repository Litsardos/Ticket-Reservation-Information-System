package com.example.DoPostServlets;


import com.example.DataBaseConnection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;

public class Search_seat extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("eventSelect");
        String seatType = request.getParameter("seatType");

        try (Connection conn = DataBaseConnection.getConnection()){

            String selectFSeatsSQL = "SELECT t.*, e.date,t.availability, e.name FROM events e RIGHT JOIN tickets t ON e.id = t.event_id WHERE t.event_id = ? AND t.seat_type = ?";



            try (PreparedStatement stmt = conn.prepareStatement(selectFSeatsSQL)) {

                stmt.setString(1, id);
                stmt.setString(2, seatType);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String outputImagePath = "D:/Desktop/CSD/HY-360/untitled/src/main/webapp/imagetemp.jpg";
                    int x = 0;
                    if (seatType.equals("VIP")) x = 550;
                    else if (seatType.equals("GA")) x = 550;
                    else if (seatType.equals("Golden")) x = 500;
                    else x = 500;

                    BufferedImage image = ImageIO.read(new File("D:/Desktop/CSD/HY-360/untitled/src/main/java/com/example/DoPostServlets/photo.jpg"));

                    Graphics2D g2d = image.createGraphics();
                    g2d.setColor(Color.BLACK);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2d.setFont(new Font("Arial Black", Font.BOLD, 37)); // Γραμματοσειρά
                    g2d.setColor(new Color(93, 89, 79)); // Χρώμα

                    g2d.drawString(rs.getString("date"), 240, image.getHeight() - 145); // Τιμή

                    g2d.setFont(new Font("Arial Black", Font.BOLD, 60)); // Γραμματοσειρά
                    g2d.setColor(new Color(93, 89, 79)); // Χρώμα
                    g2d.drawString("Available: "+rs.getString("availability"), 425, 70);

                    g2d.setFont(new Font("Arial Black", Font.BOLD, 50));
                    g2d.drawString(seatType, x, image.getHeight() - 145);
                    g2d.drawString("€"+rs.getString("price"), 750, image.getHeight() - 145);

                    g2d.setFont(new Font("Arial", Font.BOLD, 120));
                    int len = rs.getString("name").length();
                    g2d.drawString(rs.getString("name"), 565 - (len * 30), 225); // Όνομα
                    g2d.setFont(new Font("Arial", Font.BOLD, 40));
                    g2d.drawString("Event id: "+id, 500, 115); // ID

                    g2d.dispose();
                    File outputFile = new File(outputImagePath);
                    outputFile.getParentFile().mkdirs(); // Δημιουργία φακέλων
                    ImageIO.write(image, "jpg", new File(outputImagePath)); // Αποθήκευση εικόνας
                    response.sendRedirect("book_tickets1.html");
                }else{
                    response.sendRedirect("Search_seat_fail.html?error=invalid");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }
}
