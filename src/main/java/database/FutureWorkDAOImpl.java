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
}
