package app;

import database.UserProfileDAO;
import database.UserProfileDAOImpl;
import util.DatabaseConnection;
import util.MessageBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserProfileService {


    /**
     * Fetches user data as List<String>
     * Order: [firstName, lastName, email, address/work_area, userType, gender]
     */
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

    /**
     * Updates user's first name, last name, and address/work_area
     * for both Worker and Household based on userType
     */
    public static boolean updateUserProfile(String userId, String firstName, String lastName,
                                            String addressOrWorkArea, String userType) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            UserProfileDAO dao = new UserProfileDAOImpl(conn);
            return dao.updateUserProfile(userId, firstName, lastName, addressOrWorkArea, userType);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            MessageBox.showError("Update Failed", "Could not update profile");
            return false;
        }
    }
}
