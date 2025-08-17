package database;

import model.FutureWork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FutureWorkDAOImpl implements FutureWorkDAO {

    private final Connection conn;

    public FutureWorkDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // Household POV
    @Override
    public List<FutureWork> getFutureWorkByHousehold(int householdId) throws SQLException {
        List<FutureWork> list = new ArrayList<>();

        ///  can add date condition
        String sql = "SELECT aw.task_id, wr.title, aw.worker_id, wr.preferred_work_date, aw.status, aw.household_rating FROM workrequests wr LEFT JOIN assignedtasks aw ON wr.request_id = aw.request_id WHERE wr.household_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, householdId);
            /// checking
        stmt.setInt(1,2);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FutureWork fw = new FutureWork(
                        rs.getInt("task_id"),
                        rs.getString("title"),
                        rs.getInt("worker_id"),
                        rs.getDate("preferred_work_date") != null ? rs.getDate("preferred_work_date").toLocalDate() : null,
                        rs.getString("status"),
                        rs.getObject("rating") != null ? rs.getInt("rating") : null
                );
                list.add(fw);
            }
        }
        return list;
    }

    // Worker POV
    @Override
    public List<FutureWork> getUpcomingJobsByWorker(int workerId) throws SQLException {
        List<FutureWork> list = new ArrayList<>();

        String sql = "SELECT aw.task_id, wr.title, aw.household_id, wr.preferred_work_date, aw.status, aw.household_rating, wr.address FROM workrequests wr LEFT JOIN assignedtasks aw ON wr.request_id = aw.request_id WHERE aw.worker_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, workerId);
            /// checking
            stmt.setInt(1,4);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt("household_id"));
                FutureWork fw = new FutureWork(
                        rs.getInt("task_id"),
                        rs.getInt("household_id"),
                        rs.getString("title"),
                        rs.getString("address"),
                        rs.getDate("preferred_work_date") != null ? rs.getDate("preferred_work_date").toLocalDate() : null,
                        rs.getString("status"),
                        rs.getInt("household_rating")
                );
                list.add(fw);
            }
        }

        return list;
    }
}
