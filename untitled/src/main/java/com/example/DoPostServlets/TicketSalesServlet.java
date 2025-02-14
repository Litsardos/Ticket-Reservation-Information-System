package com.example.DoPostServlets;

import com.example.Action;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/TicketSalesServlet")
public class TicketSalesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set content type for HTML response
        response.setContentType("text/html; charset=UTF-8");

        // Get parameters from the request
        String seatType = request.getParameter("seat_type");
        boolean total = Boolean.parseBoolean(request.getParameter("total"));
        if(seatType == null) {
            System.out.println("seatType is null");
        }
        // Prepare to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;

        try {
            // Redirect System.out to capture method output
            System.setOut(printStream);

            // Call the function
            Action.ShowIncomeBytickets(seatType, total);

            // Restore System.out
            System.setOut(originalOut);

            // Retrieve the captured output
            String output = outputStream.toString();

            // Write HTML response
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Ticket Sales</title>");
            // Bootstrap CSS
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("<style>");
            out.println("body {");
            out.println("  height: 100vh;");
            out.println("  background: linear-gradient(135deg, #6a11cb, #2575fc);");
            out.println("  color: #fff;");
            out.println("  font-family: 'Arial', sans-serif;");
            out.println("  display: flex;");
            out.println("  justify-content: center;");
            out.println("  align-items: center;");
            out.println("  margin: 0;");
            out.println("}");
            out.println(".card {");
            out.println("  background: #ffffff;");
            out.println("  color: #333;");
            out.println("  border-radius: 20px;");
            out.println("  padding: 40px;");
            out.println("  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);");
            out.println("  transition: transform 0.3s ease, box-shadow 0.3s ease;");
            out.println("  max-width: 500px;");
            out.println("  width: 100%;");
            out.println("  text-align: center;font-weight: bold;");
            out.println("}");
            out.println(".card:hover {");
            out.println("  transform: translateY(-10px);");
            out.println("  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.3);");
            out.println("}");
            out.println("h1 {");
            out.println("  font-size: 2.2rem;");
            out.println("  font-weight: bold;");
            out.println("  margin-bottom: 1rem;");
            out.println("  color: #6a11cb;");
            out.println("}");
            out.println(".btn-primary {");
            out.println("  background: linear-gradient(135deg, #6a11cb, #2575fc);");
            out.println("  color: #fff;");
            out.println("  padding: 12px 25px;");
            out.println("  border-radius: 50px;");
            out.println("}");
            out.println(".btn-primary:hover {");
            out.println("  background: linear-gradient(135deg, #2575fc, #6a11cb);");
            out.println("}");
            out.println(".btn-secondary {");
            out.println("  background: #f8f9fa;");
            out.println("  color: #6a11cb;");
            out.println("}");
            out.println(".btn-secondary:hover {");
            out.println("  background: #ddd;");
            out.println("}");
            out.println("pre {\n" +
                    "        font-size: 300px; /* Set the font size to a larger value */\n" +
                    "        line-height: 1.5;\n" +
                    "        white-space: pre-wrap;\n" +
                    "        word-wrap: break-word;\n" +
                    "    }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container d-flex justify-content-center align-items-center'>");
            out.println("<div class='card'>");
            out.println("<h1>Ticket Sales Information</h1>");
            out.println("<div class='d-flex justify-content-around mt-4'>");
            out.println("<pre>" + output + "</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            // Ensure System.out is restored even if an error occurs
            System.setOut(originalOut);
        }
    }
}
