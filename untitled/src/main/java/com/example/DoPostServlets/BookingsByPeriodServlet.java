package com.example.DoPostServlets;

import com.example.Action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.Action.BookingsByPeriod;

@WebServlet("/BookingsByPeriodServlet")
public class BookingsByPeriodServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        int months = -1;
        String monthsParam = request.getParameter("months");
        if (monthsParam != null) {
            try {
                months = Integer.parseInt(monthsParam);
                System.out.println("months: " + monthsParam);
            } catch (NumberFormatException e) {
                System.out.println("Invalid months: " + monthsParam);
                months = 1;
            }
        } else {
            System.out.println("months: NULL");
        }

        // Redirect System.out to capture the function's output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out; // Store original System.out
        System.setOut(new PrintStream(outputStream));

        // Call the original function
        Action.BookingsByPeriod(months);

        System.setOut(originalOut);

        // Retrieve the captured output
        String rawOutput = outputStream.toString();

        // Parse the raw output to structured HTML
        String[] lines = rawOutput.split("\n");
        StringBuilder tableRows = new StringBuilder();
        boolean hasBookingsForPeriod = false;
        boolean isFirstPeriod = true;

        for (String line : lines) {
            if (line.contains("Event Name")) {
                // Split the details into columns
                hasBookingsForPeriod = true;
                String[] details = line.split(", ");
                String eventName = details[0].split(": ")[1];
                String seatType = details[1].split(": ")[1];
                String date = details[2].split(": ")[1];
                tableRows.append("<tr>");
                tableRows.append("<td>").append(eventName).append("</td>");
                tableRows.append("<td>").append(seatType).append("</td>");
                tableRows.append("<td>").append(date).append("</td>");
                tableRows.append("</tr>");
            } else if (!line.trim().isEmpty() && !line.contains("No Booking")) {
                // Add period as a header row
                if (!hasBookingsForPeriod && !isFirstPeriod) {
                    // If no bookings were found for the previous period
                    tableRows.append("<tr>");
                    tableRows.append("<td colspan='3' style='color: red;'><em>No Booking this period</em></td>");
                    tableRows.append("</tr>");
                }
                isFirstPeriod = false; // After the first period, show "No Booking" if necessary
                hasBookingsForPeriod = false;
                tableRows.append("<tr style='background-color: #f2f2f2;'>");
                tableRows.append("<td colspan='3'><strong>").append(line).append("</strong></td>");
                tableRows.append("</tr>");
            }
        }

        // Handle the last period if no bookings were found
        if (!hasBookingsForPeriod) {
            tableRows.append("<tr>");
            tableRows.append("<td colspan='3' style='color: red;'><em>No Booking this period</em></td>");
            tableRows.append("</tr>");
        }

        // Write the output as an HTML response
        PrintStream out = new PrintStream(response.getOutputStream());
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<style>");
        out.println("body {font-family: Arial, sans-serif; background-color: #6a11cb; color: white; font-size: 25px;}");
        out.println(".container {background-color: white; border-radius: 10px; padding: 20px; color: #1a1a1a;}");
        out.println("table { width: 100%; height: 100%; border-collapse: collapse; }");
        out.println("th, td { text-align: center; padding: 10px; border: 1px solid #ddd; }");
        out.println("th { background-color: #6a11cb; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
        out.println("tr:hover { background-color: #ddd; }");
        out.println("</style>");
        out.println("<div class='container mt-5'>");
        out.println("<h1 class='text-center' style='color: #6a11cb;'>Event Bookings</h1>");
        out.println("<table class='table table-bordered table-striped mt-4'>");
        out.println("<thead style='background-color: #6a11cb; color: white;'>");
        out.println("<tr>");
        out.println("<th>Event Name</th>");
        out.println("<th>Seat Type</th>");
        out.println("<th>Date</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");
        out.println(tableRows.toString());
        out.println("</tbody>");
        out.println("</table>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}


