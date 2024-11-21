import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class AdminOperations {
    public void addCandidate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Candidate Name: ");
        String candidateName = scanner.nextLine();

        String insertQuery = "INSERT INTO candidates (name) VALUES (?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, candidateName);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Candidate added successfully!");
            } else {
                System.out.println("Failed to add candidate.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
