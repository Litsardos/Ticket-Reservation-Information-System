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



@WebServlet("/most_income_event")
public class MostIncomeEventServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type to HTML
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get date range from request parameters
        String firstDate = request.getParameter("firstdate");
        String secondDate = request.getParameter("seconddate");

        try (Connection conn = DataBaseConnection.getConnection()) {

            // SQL query for event income calculation
            String selectIncomeSQL = "SELECT e.id AS event_id, e.name AS event_name,e.date, COALESCE(SUM(b.payment_amount), 0) AS total_income " +
                    "FROM events e " +
                    "LEFT JOIN bookings b ON e.id = b.event_id " +
                    "WHERE b.date IS NOT NULL AND b.date BETWEEN ? AND ? " +
                    "GROUP BY e.id, e.name " +
                    "ORDER BY total_income DESC;";

            try (PreparedStatement stmt = conn.prepareStatement(selectIncomeSQL)) {
                stmt.setString(1, firstDate);
                stmt.setString(2, secondDate);

                ResultSet rs = stmt.executeQuery();

                // Generate HTML response
                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<title>Highest Revenue Events</title>");
                out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css'>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container mt-5'>");
                out.println("<h2 class='mb-4'>Highest Revenue Events</h2>");

                if (!rs.isBeforeFirst()) {
                    out.println("<p>No income data found for the selected date range.</p>");
                } else {
                    out.println("<table class='table table-striped'>");
                    out.println("<thead><tr><th>Event ID</th><th>Event Name</th><th>Date</th><th>Total Income</th></tr></thead>");
                    out.println("<tbody>");

                    while (rs.next()) {
                        int eventId = rs.getInt("event_id");
                        String eventName = rs.getString("event_name");
                        String totalIncome = rs.getString("total_income");
                        String date = rs.getString("date");

                        out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td></tr>%n", eventId, eventName,date, "€"+totalIncome);
                    }

                    out.println("</tbody>");
                    out.println("</table>");
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
