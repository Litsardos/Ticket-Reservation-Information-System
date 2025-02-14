package com.example.DoPostServlets;

import com.example.Action;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ShowTickets extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String webappFolder = "D:/Desktop/CSD/HY-360/untitled/src/main/webapp/tickets/";
        String javaFolder = "D:/Desktop/CSD/HY-360/untitled/src/main/java/com/example/DoPostServlets/tickets/";
        deleteFolderContents(webappFolder);
        deleteFolderContents(javaFolder);

        String[] a = Action.Findavailabletickets();
        int i = 0;
        while (i < a.length) {
            String imagePath = "D:/Desktop/CSD/HY-360/untitled/src/main/java/com/example/DoPostServlets/photo.jpg"; // Διαδρομή εικόνας
            String outputImagePath = "D:/Desktop/CSD/HY-360/untitled/src/main/webapp/tickets/image" + (i / 6 + 1) + ".jpg";

            String name = a[i]; // Ημερομηνία
            String id = "Event id: " + a[i + 1]; // Τύπος θέσης (VIP ή Golden)
            String date = a[i + 2]; // Τιμή
            String seatType = a[i + 3]; // Όνομα που προστίθεται
            String price = "€" + a[i + 4]; // Τιμή
            String amount = a[i + 5];
            String outputImagePathS = "D:/Desktop/CSD/HY-360/untitled/src/main/java/com/example/DoPostServlets/tickets/" + (i / 6 + 1) +"-"+a[i + 1]+"-"+seatType+"-"+amount+ ".jpg";
            i = i + 6;

            try {
                // Φόρτωση εικόνας
                BufferedImage image = ImageIO.read(new File(imagePath));

                // Δημιουργία Graphics2D αντικειμένου
                Graphics2D g2d = image.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setFont(new Font("Arial Black", Font.BOLD, 37)); // Γραμματοσειρά
                g2d.setColor(new Color(93, 89, 79)); // Χρώμα

                g2d.drawString(date, 240, image.getHeight() - 145); // Τιμή
                int x = 0;
                if (seatType.equals("VIP")) x = 550;
                else if (seatType.equals("GA")) x = 550;
                else if (seatType.equals("Golden")) x = 500;
                else x = 500;

                g2d.setFont(new Font("Arial Black", Font.BOLD, 50));
                g2d.drawString(seatType, x, image.getHeight() - 145);
                g2d.drawString(price, 750, image.getHeight() - 145);

                g2d.setFont(new Font("Arial", Font.BOLD, 120));
                int len = name.length();
                g2d.drawString(name, 565 - (len * 30), 225); // Όνομα
                g2d.setFont(new Font("Arial", Font.BOLD, 40));
                g2d.drawString(id, 500, 115); // ID

                g2d.dispose();


                File outputFile = new File(outputImagePath);
                outputFile.getParentFile().mkdirs(); // Δημιουργία φακέλων
                ImageIO.write(image, "jpg", new File(outputImagePath)); // Αποθήκευση εικόνας

                File outputFileS = new File(outputImagePathS);
                outputFileS.getParentFile().mkdirs(); // Δημιουργία φακέλων
                ImageIO.write(image, "jpg", new File(outputImagePathS)); // Αποθήκευση εικόνας

            } catch (IOException e) {
                System.out.println("Σφάλμα κατά τη φόρτωση ή αποθήκευση της εικόνας.");
                e.printStackTrace();
            }
        }



        response.sendRedirect("ShowTickets.html");

    }
    private void deleteFolderContents(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
    }

}