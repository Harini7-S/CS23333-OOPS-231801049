import javax.swing.*;
import java.sql.*;
import java.util.Scanner;

public class VotingMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Display available candidates to the user
        displayCandidates();

        // Ask the user to input their user ID
        int userId = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Your User ID:", "User ID", JOptionPane.PLAIN_MESSAGE));

        // Check if the user has already voted
        if (hasUserVoted(userId)) {
            JOptionPane.showMessageDialog(null, "You have already voted.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Ask the user to choose a candidate
            int candidateId = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Candidate ID to Vote:", "Vote", JOptionPane.PLAIN_MESSAGE));
            
            // Cast the vote
            castVote(userId, candidateId);
        }
    }

    // Display all available candidates to the user
    private static void displayCandidates() {
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

                JOptionPane.showMessageDialog(null, candidates.toString(), "Available Candidates", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching candidates: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Check if the user has already voted
    private static boolean hasUserVoted(int userId) {
        try {
            // Connect to the database
            Connection connection = DatabaseConnection.getConnection();
            if (connection != null) {
                // SQL query to check if the user has already voted
                String sql = "SELECT * FROM votes WHERE user_id = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                return rs.next(); // If a record exists, it means the user has already voted
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking if user has voted: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Cast the vote for the selected candidate
    private static void castVote(int userId, int candidateId) {
        Connection connection = null;
        try {
            // Connect to the database
            connection = DatabaseConnection.getConnection();
            if (connection != null) {
                // Start a transaction
                connection.setAutoCommit(false);

                // SQL query to insert the vote
                String sql = "INSERT INTO votes (user_id, candidate_id) VALUES (?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setInt(2, candidateId);
                stmt.executeUpdate();

                // Commit the transaction
                connection.commit();

                JOptionPane.showMessageDialog(null, "Vote cast successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    // Rollback transaction if an error occurs
                    connection.rollback();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error rolling back: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Error casting vote: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto commit mode
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error resetting auto-commit: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
