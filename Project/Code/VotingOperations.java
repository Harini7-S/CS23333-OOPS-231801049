import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class VotingOperations {
    public void castVote() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Available Candidates: ");

        String selectQuery = "SELECT candidate_id, name FROM candidates";
        String checkVoteQuery = "SELECT COUNT(*) FROM votes WHERE user_id = ?";
        String insertVoteQuery = "INSERT INTO votes (user_id, candidate_id) VALUES (?, ?)";
        String updateVoteCountQuery = "UPDATE candidates SET votes_count = votes_count + 1 WHERE candidate_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {

            ResultSet resultSet = selectStmt.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("candidate_id") + ". " + resultSet.getString("name"));
            }

            System.out.println("Enter Your User ID: ");
            int userId = scanner.nextInt();

            // Check if the user has already voted
            try (PreparedStatement checkVoteStmt = connection.prepareStatement(checkVoteQuery)) {
                checkVoteStmt.setInt(1, userId);
                ResultSet checkResult = checkVoteStmt.executeQuery();
                checkResult.next();
                int voteCount = checkResult.getInt(1);

                if (voteCount > 0) {
                    System.out.println("You have already voted.");
                    return;
                }

                System.out.println("Enter Candidate ID to Vote: ");
                int candidateId = scanner.nextInt();

                try (PreparedStatement voteStmt = connection.prepareStatement(insertVoteQuery);
                     PreparedStatement updateVoteStmt = connection.prepareStatement(updateVoteCountQuery)) {

                    // Insert the vote
                    voteStmt.setInt(1, userId);
                    voteStmt.setInt(2, candidateId);
                    voteStmt.executeUpdate();

                    // Update the vote count
                    updateVoteStmt.setInt(1, candidateId);
                    updateVoteStmt.executeUpdate();

                    System.out.println("Vote cast successfully!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
