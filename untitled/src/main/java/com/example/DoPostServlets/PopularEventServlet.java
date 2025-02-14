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

@WebServlet("/popular_event")
public class PopularEventServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type to HTML
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DataBaseConnection.getConnection()) {

            // SQL query for popular events
            String selectIncomeSQL = "SELECT e.id AS event_id, e.name AS event_name, COALESCE(SUM(b.ticket_amount), 0) AS total_tickets " +
                    "FROM events e " +
                    "LEFT JOIN bookings b ON e.id = b.event_id " +
                    "GROUP BY e.id, e.name " +
                    "ORDER BY total_tickets DESC;";

            try (PreparedStatement stmt = conn.prepareStatement(selectIncomeSQL)) {

                ResultSet rs = stmt.executeQuery();

                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<title>Most Popular Events</title>");
                out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css'>");
                out.println("<style>");
                out.println("body { font-family: Arial, sans-serif; background-color: #f8f9fa; color: #1a1a1a; }");
                out.println(".content-section { border: 2px solid #6a11cb; border-radius: 10px; background-color: linear-gradient(135deg, #6a11cb, #2575fc); box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); padding: 20px; }");
                out.println("table { width: 100%; height: 100%; border-collapse: collapse; }");
                out.println("th, td { text-align: center ; padding: 10px; border: 1px solid #ddd; }");
                out.println("th { background-color: #6a11cb; color: white; }");
                out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
                out.println("tr:hover { background-color: #ddd; }");
                out.println("</style>");
                out.println("</head><body>");
                out.println("<div class='content-section'>");

                if (!rs.isBeforeFirst()) {
                    out.println("<p>No booking data found.</p>");
                } else {
                    out.println("<table>");
                    out.println("<tr><th>Event ID</th><th>Event Name</th><th>Total Tickets</th></tr>");
                    out.println("<tbody>");

                    while (rs.next()) {
                        int eventId = rs.getInt("event_id");
                        String eventName = rs.getString("event_name");
                        int totalTickets = rs.getInt("total_tickets");

                        out.printf("<tr><td>%d</td><td>%s</td><td>%d</td></tr>%n", eventId, eventName, totalTickets);
                    }

                    out.println("</tbody></table>");
                }

                out.println("</div>");
                out.println("</body>");
                out.println("</html>");

            } catch (SQLException e) {
                out.println("<p>Error during SQL execution: " + e.getMessage() + "</p>");
            }

        } catch (SQLException e) {
            out.println("<p>Error during database connection: " + e.getMessage() + "</p>");
        }
    }
}
