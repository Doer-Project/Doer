package database.household;

import model.household.WorkRequest;
import util.DBConnection;

import java.sql.*;

public class WorkReqDAOImpl implements WorkReqDAO {

    private static Connection connection;

    public WorkReqDAOImpl() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Insert new work request
    public boolean insert(WorkRequest request) {
        String sql = "INSERT INTO workrequests (household_id, title, category, description, preferred_work_date, address, city, pincode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, request.getHouseholdId());
            stmt.setString(2, request.getTitle());
            stmt.setString(3, request.getCategory());
            stmt.setString(4, request.getDescription());
            stmt.setDate(5, Date.valueOf(request.getRequestDate()));
            stmt.setString(6, request.getArea());
            stmt.setString(7, request.getCity());
            stmt.setInt(8, request.getPinCode());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting work request: " + e.getMessage());
            return false;
        }
    }
}
