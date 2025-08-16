//package app;
//
//import database.ProfileDAO;
//import database.ProfileDAOImpl;
//import util.DatabaseConnection;
//import util.MessageBox;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class UserProfileService {
//
//    private ProfileDAO dao;
//    private ProfileDAO profileDAO;
//
//    // Initialize DAO with connection
//    public UserProfileService()  {
//        Connection conn = null;
//        try {
//            conn = DatabaseConnection.getConnection();
//        } catch (SQLException e) {
//            MessageBox.showError("Database Error", "Failed to connect to the database:\n" + e.getMessage());
//            return;
//        }
//        this.dao = new ProfileDAOImpl(conn);
//    }
//
//    // Fetch user by username
//    public User getUserByUsername(String username) {
//        try {
//            return dao.getUserByUsername(username);
//        } catch (SQLException e) {
//            MessageBox.showError("Database Error", "Failed to fetch user:\n" + e.getMessage());
//            return null;
//        }
//    }
//
//    public boolean updateUser(User user, boolean confirm) throws SQLException {
//        return profileDAO.updateUser(user, confirm);
//    }
//}
