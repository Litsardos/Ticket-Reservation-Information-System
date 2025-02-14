package com.example.DoPostServlets;

import com.example.Action;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.URLEncoder;

import static java.awt.SystemColor.window;

public class BookTicket extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String amountParam = request.getParameter("amount");

        Cookie[] cookies = request.getCookies();
        String userId = null;
        String imageName = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    userId = cookie.getValue();
                }
                if ("imageName".equals(cookie.getName())) {
                    imageName = cookie.getValue();
                }
            }
        } else {
            System.out.println("No cookies found!");
        }
        System.out.println(userId);
        System.out.println(imageName);
        String name=null;
        char photoid = ' ';
        if (imageName != null && imageName.length() > 1 && imageName.lastIndexOf(".") > 1) {
            photoid = imageName.charAt(imageName.lastIndexOf(".") - 1);
        } else {
            System.out.println("No image found or invalid image format.");
            response.sendRedirect("error.html");
            return;
        }
        File folder = new File("D:/Desktop/CSD/HY-360/untitled/src/main/java/com/example/DoPostServlets/tickets/");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                // Ελέγχουμε αν το αρχείο είναι εικόνα και αν το όνομα αρχίζει με το γράμμα 'g'
                if (file.getName().toLowerCase().startsWith(String.valueOf(photoid)) && file.isFile()) {
                    name=file.getName();
                }
            }
        } else {
            System.out.println("Ο φάκελος είναι κενός ή δεν έχει αρχεία.");
        }
        if (name == null) {
            System.out.println("Error: No matching file found.");
            response.sendRedirect("error.html");
            return;
        }

        String[] parts = name.split("-");
        String event_id = parts[1];   // Αποθηκεύει το έτος ως String
        String seat_type = parts[2];  // Αποθηκεύει το μήνα ως String
        String availability = parts[3];    // Αποθηκεύει την ημέρα ως String
        String availabilityNumberStr = availability.split("\\.")[0];  // Θα κρατήσει το "3" από "3.jpg"

        int availabilityInt = -1;

        try {
            availabilityInt = Integer.parseInt(availabilityNumberStr);  // Προσπαθούμε να το μετατρέψουμε σε ακέραιο
        } catch (NumberFormatException e) {
            System.out.println("Η availability δεν μπορεί να μετατραπεί σε αριθμό: " + availabilityNumberStr);
        }
        if(userId==null){
            System.out.println("Error: No 1 id found.");
        }
        if(event_id==null){
            System.out.println("Error: No 2 id found.");
        }
        if(amountParam==null){
            System.out.println("Error: No 3 id found.");
        }
        if(seat_type==null){
            System.out.println("Error: No 4 id found.");
        }

        Action.BookTicket(Integer.parseInt(userId),Integer.parseInt(event_id),seat_type,Integer.parseInt(amountParam));
        new ShowTickets();
        if(Integer.parseInt(amountParam)>availabilityInt){
            response.sendRedirect("sh1.html");
        }else{
            response.sendRedirect("main.html");
        }

    }
}

