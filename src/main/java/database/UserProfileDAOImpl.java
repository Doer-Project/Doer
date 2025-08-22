package database;

import datastructures.CustomList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileDAOImpl implements UserProfileDAO {
    private final Connection conn;

    public UserProfileDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public CustomList<String> fetchUserDataList(String userId) throws SQLException {
        String query = "SELECT u.first_name, u.last_name, u.email, u.role, w.work_area, h.address, u.gender FROM users u LEFT JOIN workers w ON w.worker_id = u.user_id LEFT JOIN households h ON h.household_id = u.user_id WHERE u.user_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();

            CustomList<String> userData = new CustomList<>();
            if (rs.next()) {
                userData.add(rs.getString("first_name"));
                userData.add(rs.getString("last_name"));
                userData.add(rs.getString("email"));
                if(rs.getString("role").equals("household")){
                    userData.add(rs.getString("address"));
                } else {
                    userData.add(rs.getString("work_area"));
                }
                userData.add(rs.getString("role"));
                userData.add(rs.getString("gender"));
//                userData.add(rs.getString("profile_pic"));
            }
            return userData;
        }
    }

    public boolean updateUserProfile(String userId, String firstName, String lastName, String addressOrWorkArea, String userType) throws SQLException {
        String query = "";

        if ("Worker".equalsIgnoreCase(userType)) {
            query = "UPDATE workers w JOIN users u ON w.worker_id = u.user_id " +
                    "SET u.first_name = ?, u.last_name = ?, w.work_area = ? " +
                    "WHERE u.user_id = ?";
        } else if ("Household".equalsIgnoreCase(userType)) {
            query = "UPDATE households h JOIN users u ON h.household_id = u.user_id " +
                    "SET u.first_name = ?, u.last_name = ?, h.address = ? " +
                    "WHERE u.user_id = ?";
        }

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, addressOrWorkArea);
            pst.setString(4, userId);
            /// checking
//        pst.setInt(4,1);
            return pst.executeUpdate() > 0;
        }
    }

    @Override
    public byte[] getProfilePicture(String userId) throws SQLException {
        String query = "SELECT prof_pic FROM users WHERE user_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getBytes("prof_pic");
            }
        }
        return null;
    }

    @Override
    public boolean updateProfilePicture(String userId, byte[] imageBytes) throws SQLException {
        String query = "UPDATE users SET prof_pic = ? WHERE user_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setBytes(1, imageBytes);
            pst.setString(2, userId);
            return pst.executeUpdate() > 0;
        }
    }

}
