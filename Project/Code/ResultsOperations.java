import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResultsOperations {
    public void displayResults() {
        String query = "SELECT name, votes_count FROM candidates";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Voting Results:");
            while (resultSet.next()) {
                String candidateName = resultSet.getString("name");
                int votesCount = resultSet.getInt("votes_count");
                System.out.println(candidateName + ": " + votesCount + " votes");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
