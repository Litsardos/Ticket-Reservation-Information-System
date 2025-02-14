package com.example.DoPostServlets;

import com.example.DataBaseConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/seat_status")
public class SeatStatusServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DataBaseConnection.getConnection()) {
            // HTML Header
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Seat Availability and Booking Status</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background: linear-gradient(135deg, #6a11cb, #2575fc); color: #ffffff; margin: 0; padding: 0; }");
            out.println(".content-section { border: none; background: transparent; padding: 20px; margin-bottom: 20px; }");
            out.println("h3 { color: #ffffff; font-weight: bold; text-align: center; margin-bottom: 20px; }");
            out.println("table { width: 100%; border-collapse: collapse; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }");
            out.println("th, td { text-align: center; padding: 10px; border: 1px solid #ddd; color: #000000; }"); // Κοινό στυλ για όλα τα th και td
            out.println("th { background-color: #6a11cb; color: #ffffff; font-weight: bold; }"); // Ειδικό στυλ για th κεφαλίδες
            out.println("tr:nth-child(even) { background-color: #f9f9f9; }"); // Ελαφρύ γκρι για τις ζυγές σειρές
            out.println("tr:hover { background-color: #f2f2f2; }"); // Εφέ hover για τις σειρές
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container mt-5'>");

            // Available Seats Section
            out.println("<h3>Available Seats</h3>");
            out.println("<div class='content-section'>");
            String availableSeatsSQL = "SELECT t.*, e.date, e.name FROM events e RIGHT JOIN tickets t ON e.id = t.event_id WHERE t.availability>0";
            try (PreparedStatement stmt = conn.prepareStatement(availableSeatsSQL)) {
                ResultSet rs = stmt.executeQuery();
                out.println("<table>");
                out.println("<tr><th>Event ID</th><th>Event Name</th><th>Seat Type</th><th>Availability</th></tr>");
                while (rs.next()) {
                    int eventId = rs.getInt("event_id");
                    String eventName = rs.getString("name");
                    String seatType = rs.getString("seat_type");
                    String availability = rs.getString("availability");
                    out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td></tr>", eventId, eventName, seatType, availability);
                }
                out.println("</table>");
            }
            out.println("</div>");

            // Booked Seats Section
            out.println("<div class='content-section'>");
            out.println("<h3>Booked Seats</h3>");
            String bookedSeatsSQL = "SELECT b.*, e.name FROM events e RIGHT JOIN bookings b ON e.id = b.event_id ORDER BY event_id ASC";
            try (PreparedStatement stmt = conn.prepareStatement(bookedSeatsSQL)) {
                ResultSet rs = stmt.executeQuery();
                out.println("<table>");
                out.println("<tr><th>Event ID</th><th>Event Name</th><th>Seat Type</th><th>Booked By</th></tr>");
                while (rs.next()) {
                    int eventId = rs.getInt("event_id");
                    String eventName = rs.getString("name");
                    String seatType = rs.getString("seat_type");
                    String customerId = rs.getString("customer_id");
                    out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>Customer: %s</td></tr>", eventId, eventName, seatType, customerId);
                }
                out.println("</table>");
            }
            out.println("</div>");

            out.println("</div>"); // Container End
            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Error fetching data: " + e.getMessage() + "</p>");
        }
    }
}
