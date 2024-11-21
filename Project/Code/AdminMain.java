import javax.swing.*;
import java.sql.*;
import java.util.Scanner;

public class AdminMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Infinite loop for the Admin menu until the user exits
        while (true) {
            // Show the menu to the Admin
            String menu = "Admin Menu:\n"
                    + "1. Add Candidate\n"
                    + "2. View All Candidates\n"
                    + "3. View Voting Results\n"
                    + "4. Exit";
            String choice = JOptionPane.showInputDialog(null, menu, "Admin Menu", JOptionPane.QUESTION_MESSAGE);

            // Handle the menu choice
            switch (choice) {
                case "1":
                    // Add new candidate
                    addCandidate();
                    break;
                case "2":
                    // View all candidates
                    viewAllCandidates();
                    break;
                case "3":
                    // View voting results
                    viewVotingResults();
                    break;
                case "4":
                    // Exit the program
                    JOptionPane.showMessageDialog(null, "Exiting the program.", "Exit", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice! Please select a valid option.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    // Add a new candidate to the database
    private static void addCandidate() {
        String candidateName = JOptionPane.showInputDialog(null, "Enter the name of the candidate:", "Add Candidate", JOptionPane.PLAIN_MESSAGE);
        if (candidateName != null && !candidateName.trim().isEmpty()) {
            try {
                // Connect to the database
                Connection connection = DatabaseConnection.getConnection();
                if (connection != null) {
                    // SQL query to add a new candidate
                    String sql = "INSERT INTO candidates (name) VALUES (?)";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, candidateName);
                    stmt.executeUpdate(); // Execute the insert query

                    JOptionPane.showMessageDialog(null, "Candidate added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error adding candidate: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Candidate name is required!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // View all candidates in the database
    private static void viewAllCandidates() {
        try {
            // Connect to the database
            Connection connection = DatabaseConnection.getConnection();
            if (connection != null) {
                // SQL query to fetch all candidates
                String sql = "SELECT * FROM candidates";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                StringBuilder candidates = new StringBuilder("Available Candidates:\n");
                while (rs.next()) {
                    candidates.append(rs.getInt("candidate_id")).append(". ").append(rs.getString("name")).append("\n");
                }

                JOptionPane.showMessageDialog(null, candidates.toString(), "All Candidates", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching candidates: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // View voting results (how many votes each candidate has)
    private static void viewVotingResults() {
        try {
            // Connect to the database
            Connection connection = DatabaseConnection.getConnection();
            if (connection != null) {
                // SQL query to fetch voting results
                String sql = "SELECT c.name, COUNT(v.vote_id) AS vote_count FROM candidates c LEFT JOIN votes v ON c.candidate_id = v.candidate_id GROUP BY c.candidate_id";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                StringBuilder results = new StringBuilder("Voting Results:\n");
                while (rs.next()) {
                    results.append(rs.getString("name")).append(": ").append(rs.getInt("vote_count")).append(" votes\n");
                }

                JOptionPane.showMessageDialog(null, results.toString(), "Voting Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching results: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
