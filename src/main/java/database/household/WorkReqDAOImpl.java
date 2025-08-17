package database.household;

import model.household.WorkRequest;

import java.sql.*;

public class WorkReqDAOImpl implements WorkReqDAO {

    private Connection connection;

    public WorkReqDAOImpl(Connection connection) {
        this.connection = connection;
    }

    // Insert new work request
    public boolean insert(WorkRequest request) {
        String sql = "INSERT INTO work_requests " +
                "(title, description, category_id, city, area, budget, request_date, household_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, request.getTitle());
            stmt.setString(2, request.getDescription());
            stmt.setInt(3, request.getCategoryId());
            stmt.setString(4, request.getCity());
            stmt.setString(5, request.getArea());
            stmt.setDouble(6, request.getBudget());
            stmt.setDate(7, Date.valueOf(request.getRequestDate()));
            stmt.setString(8, request.getHouseholdId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // You can also add methods like:
    // getById(int id), getAllByHousehold(int householdId), update(WorkRequest), delete(int id)
}
