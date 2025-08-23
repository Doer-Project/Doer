package database;

import datastructures.CustomList;
import model.FutureWork;
import util.DBConnection;

import java.sql.*;

public class FutureWorkDAOImpl implements FutureWorkDAO {

    private Connection connection;

    public FutureWorkDAOImpl() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    // Household POV: fetch future work created by a household
    @Override
    public CustomList<FutureWork> getFutureWorkByHousehold(String householdId){
        CustomList<FutureWork> list = new CustomList<>();

        String sql = "SELECT task_id, worker_id, title, date, " +
                "start_time, end_time, cost, status " +
                "FROM futurework WHERE household_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, householdId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FutureWork fw = new FutureWork(
                        rs.getInt("task_id"),
                        rs.getString("title"),
                        rs.getString("worker_id"),
                        rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null,
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getBigDecimal("cost") != null ? rs.getBigDecimal("cost").toString() : null,
                        rs.getString("status")
                );
                list.add(fw);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    // Worker POV: fetch upcoming jobs for a worker
    @Override
    public CustomList<FutureWork> getUpcomingJobsByWorker(String workerId){
        CustomList<FutureWork> list = new CustomList<>();

        String sql = "SELECT task_id, household_id, title, address, date, " +
                "start_time, end_time, cost, status " +
                "FROM futurework WHERE worker_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, workerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FutureWork fw = new FutureWork(
                        rs.getInt("task_id"),
                        rs.getString("household_id"),
                        rs.getString("title"),
                        rs.getString("address"),
                        rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null,
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getBigDecimal("cost") != null ? rs.getBigDecimal("cost").toString() : null,
                        rs.getString("status")
                );
                list.add(fw);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public boolean rateWork(int taskId, int rating, String review) {
        boolean oldAutoCommit = true;
        try {
            // capture current autocommit inside try so any SQLException is handled here
            oldAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            // Update base table assignedtasks: set rating, optional review, and mark as 'rated'
            String updSql = "UPDATE assignedtasks SET household_rating = ?, household_review = ?, status = 'rated' WHERE task_id = ? AND LOWER(status) IN ('completed','complete')";
            int updated;
            try (PreparedStatement upd = connection.prepareStatement(updSql)) {
                upd.setInt(1, rating);
                if (review != null && !review.isEmpty()) {
                    upd.setString(2, review);
                } else {
                    upd.setNull(2, Types.VARCHAR);
                }
                upd.setInt(3, taskId);
                updated = upd.executeUpdate();
            }

            if (updated == 0) {
                connection.rollback();
                return false;
            }

            // Also reflect status in workrequests
            String updReq = "UPDATE workrequests SET status = 'rated' WHERE request_id = (SELECT request_id FROM assignedtasks WHERE task_id = ?)";
            try (PreparedStatement upd2 = connection.prepareStatement(updReq)) {
                upd2.setInt(1, taskId);
                upd2.executeUpdate();
            }

            // Compute and update worker rating average (use double)
            String workerId;
            String selWorker = "SELECT worker_id FROM assignedtasks WHERE task_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(selWorker)) {
                ps.setInt(1, taskId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        connection.rollback();
                        return false;
                    }
                    workerId = rs.getString(1);
                }
            }

            Double avgRating = null;
            String avgSql = "SELECT ROUND(AVG(household_rating), 2) FROM assignedtasks WHERE worker_id = ? AND household_rating IS NOT NULL";
            try (PreparedStatement ps = connection.prepareStatement(avgSql)) {
                ps.setString(1, workerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double val = rs.getDouble(1);
                        if (!rs.wasNull()) {
                            avgRating = val;
                        }
                    }
                }
            }
            if (avgRating == null) {
                avgRating = (double) rating;
            }
            // ensure 2-decimal rounding in Java as well
            avgRating = Math.round(avgRating * 100.0) / 100.0;

            String updWorker = "UPDATE workers SET rating_avg = ? WHERE worker_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updWorker)) {
                ps.setDouble(1, avgRating);
                ps.setString(2, workerId);
                ps.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException ex) {
            try { connection.rollback(); } catch (SQLException ignore) { }
            throw new RuntimeException(ex);
        } finally {
            try { connection.setAutoCommit(oldAutoCommit); } catch (SQLException ignore) { }
        }
    }
}
