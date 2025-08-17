package app;

import database.UserProfileDAO;
import database.UserProfileDAOImpl;
import util.DatabaseConnection;
import util.MessageBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserProfileService {

    public static List<String> getUserDataList(String userId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            UserProfileDAO dao = new UserProfileDAOImpl(conn);
            return dao.fetchUserDataList(userId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            MessageBox.showError("Connection Failed", "Database not connected successfully");
            return null;
        }
    }
}
