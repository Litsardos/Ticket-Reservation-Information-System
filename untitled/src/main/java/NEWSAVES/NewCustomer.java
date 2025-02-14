package NEWSAVES;

import com.example.DataBaseConnection;

import java.sql.*;

public class NewCustomer {

    // Constructor που δέχεται όλα τα πεδία
    public NewCustomer(String loginname, String password, String name, String lastname, String email,String phonenumber,
                       String creditCardNumber, String creditCardCVV, String creditCardDate,int balance) {


        String insertCustomerSQL = "INSERT INTO customers (loginname, password, name, lastname, email,phonenumber ,C_C_Num, C_C_CVV, C_C_Date,C_C_Balance) "
                + "VALUES (?, ?,?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertCustomerSQL)) {

            // Ορισμός παραμέτρων στη SQL
            pstmt.setString(1, loginname);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, lastname); // Δέχεται null εάν δεν δοθεί
            pstmt.setString(5, email);
            pstmt.setString(6, phonenumber);
            pstmt.setString(7, creditCardNumber); // Δέχεται null εάν δεν δοθεί
            pstmt.setString(8, creditCardCVV);    // Δέχεται null εάν δεν δοθεί
            pstmt.setString(9, creditCardDate);   // Δέχεται null εάν δεν δοθεί
            pstmt.setInt(10, balance);

            // Εκτέλεση εντολής
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Customer added successfully!");
            } else {
                System.out.println("Failed to add customer.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while adding customer: " + e.getMessage());
        }
    }

    // Constructor που δέχεται μόνο υποχρεωτικά πεδία
    public NewCustomer(String loginname, String password, String name) {
        this(loginname, password, name, null,null, null, null, null, null,300);
    }
}
