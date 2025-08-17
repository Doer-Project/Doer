package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserProfileDAOImpl implements UserProfileDAO {
    private final Connection conn;

    public UserProfileDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<String> fetchUserDataList(String userId) throws SQLException {
        String query = "SELECT first_name, last_name, email, address, user_type, gender, profile_pic FROM users WHERE user_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();

            List<String> userData = new ArrayList<>();
            if (rs.next()) {
                userData.add(rs.getString("first_name"));
                userData.add(rs.getString("last_name"));
                userData.add(rs.getString("email"));
                userData.add(rs.getString("address"));
                userData.add(rs.getString("user_type"));
                userData.add(rs.getString("gender"));
                userData.add(rs.getString("profile_pic"));
            }
            return userData;
        }
    }
}
