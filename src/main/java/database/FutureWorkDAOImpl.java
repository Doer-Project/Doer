package database;

import datastructures.CustomList;
import model.FutureWork;

import java.sql.*;

public class FutureWorkDAOImpl implements FutureWorkDAO {

    private final Connection conn;

    public FutureWorkDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // Household POV: fetch future work created by a household
    @Override
    public CustomList<FutureWork> getFutureWorkByHousehold(String householdId) throws SQLException {
        CustomList<FutureWork> list = new CustomList<>();

        String sql = "SELECT task_id, worker_id, title, date, " +
                "start_time, end_time, cost, status " +
                "FROM futurework WHERE household_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        }
        return list;
    }


    // Worker POV: fetch upcoming jobs for a worker
    @Override
    public CustomList<FutureWork> getUpcomingJobsByWorker(String workerId) throws SQLException {
        CustomList<FutureWork> list = new CustomList<>();

        String sql = "SELECT task_id, household_id, title, address, date, " +
                "start_time, end_time, cost, status " +
                "FROM futurework WHERE worker_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        }
        return list;
    }

    @Override
    public boolean rateWork(int taskId, int rating, String review) throws SQLException {
        boolean oldAutoCommit = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);

            // Update base table assignedtasks: set rating, optional review, and mark as 'rated'
            String updSql = "UPDATE assignedtasks SET household_rating = ?, household_review = ?, status = 'rated' WHERE task_id = ? AND LOWER(status) IN ('completed','complete')";
            int updated;
            try (PreparedStatement upd = conn.prepareStatement(updSql)) {
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
                conn.rollback();
                return false;
            }

            // Also reflect status in workrequests
            String updReq = "UPDATE workrequests SET status = 'rated' WHERE request_id = (SELECT request_id FROM assignedtasks WHERE task_id = ?)";
            try (PreparedStatement upd2 = conn.prepareStatement(updReq)) {
                upd2.setInt(1, taskId);
                upd2.executeUpdate();
            }

            // Compute and update worker rating average (use double)
            String workerId;
            String selWorker = "SELECT worker_id FROM assignedtasks WHERE task_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(selWorker)) {
                ps.setInt(1, taskId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    workerId = rs.getString(1);
                }
            }

            Double avgRating = null;
            String avgSql = "SELECT ROUND(AVG(household_rating), 2) FROM assignedtasks WHERE worker_id = ? AND household_rating IS NOT NULL";
            try (PreparedStatement ps = conn.prepareStatement(avgSql)) {
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
            try (PreparedStatement ps = conn.prepareStatement(updWorker)) {
                ps.setDouble(1, avgRating);
                ps.setString(2, workerId);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            try { conn.setAutoCommit(oldAutoCommit); } catch (SQLException ignore) { }
        }
    }
}
