import java.sql.*;

public class DatabaseConnection {

    // Method to establish a connection to the database
    public static Connection getConnection() {
        try {
            // Database connection details
            String url = "jdbc:mysql://localhost:3306/voting_system"; // Replace with your actual database name
            String username = "root";  // Your MySQL username
            String password = "";      // Your MySQL password

            // Establish connection to the database
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            // Handle any SQL connection errors
            System.err.println("Connection failed: " + e.getMessage());
            return null; // Return null if connection fails
        }
    }
}
