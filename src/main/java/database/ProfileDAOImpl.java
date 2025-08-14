package database;

import model.Household;
import model.User;
import model.Worker;
import util.DatabaseConnection;

import java.sql.*;

public class ProfileDAOImpl implements ProfileDAO {

    private Connection conn;

    public ProfileDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String userType = rs.getString("user_type");

                if ("household".equalsIgnoreCase(userType)) {
                    return new Household(
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("address"),
                            rs.getString("city"),
                            rs.getString("pin_code"),
                            null  //  NOT passing password hash to the UI
//                            rs.getString("password_hash")
                    );
                } else if ("worker".equalsIgnoreCase(userType)) {
                    return new Worker(
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("category"),
                            rs.getInt("experience"),
                            null,  // NOT passing password hash to the UI
//                            rs.getString("password_hash"),
                            rs.getString("preferred_city")
                    );
                }
            }
        }
        return null; // user not found
    }

    public boolean updateUser(User user, boolean confirm) throws SQLException {
        conn.setAutoCommit(false); // start transaction
        String sql = "UPDATE users SET first_name=?, last_name=?, gender=?, photo_path=? WHERE username=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getGender());
            stmt.setString(4, user.getPhotoPath());
            stmt.setString(5, user.getUserName());

            boolean updated =  stmt.executeUpdate() > 0;
            if (updated && confirm) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true); // restore default
        }
    }
}
