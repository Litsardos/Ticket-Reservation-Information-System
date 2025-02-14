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

@WebServlet("/event_income")
public class EventIncomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Event Income</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css' rel='stylesheet'/>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f8f9fa; color: #1a1a1a; }");
        out.println(".content-section { border: 2px solid #6a11cb; border-radius: 10px; background-color: linear-gradient(135deg, #6a11cb, #2575fc); box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }");
        out.println("table { width: 100%; height: 100%; border-collapse: collapse; }");
        out.println("th, td { text-align: center ;  border: 1px solid #ddd; }");
        out.println("th { background-color: #6a11cb; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
        out.println("tr:hover { background-color: #ddd; }");
        out.println("</style>");
        out.println("</head><body>");

        // Αφαιρώ τον τίτλο εδώ!
        out.println("<div class='content-section'>");
        out.println("<table>");
        out.println("<tr><th>Event ID</th><th>Event Name</th><th>Total Income</th></tr>");

        try (Connection conn = DataBaseConnection.getConnection()) {
            String selectIncomeSQL = "SELECT e.id, e.name, COALESCE(SUM(b.payment_amount), 0) AS total_income "
                    + "FROM events e LEFT JOIN bookings b ON e.id = b.event_id "
                    + "GROUP BY e.id, e.name ORDER BY e.id ASC;";

            try (PreparedStatement stmt = conn.prepareStatement(selectIncomeSQL)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int eventId = rs.getInt("id");
                    String eventName = rs.getString("name");
                    float totalIncome = rs.getFloat("total_income");

                    // Print each row in the table
                    out.printf("<tr><td>%d</td><td>%s</td><td>%.2f</td></tr>", eventId, eventName, totalIncome);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Error fetching data: " + e.getMessage() + "</p>");
        }

        out.println("</table>");
        out.println("</div>");
        out.println("</body></html>");
        out.close();
    }
}
