package database;

import datastructures.CustomList;
import model.PastTask;
import java.sql.*;

public class PastWorkDAOImpl implements PastWorkDAO {
    private final Connection conn;

    public PastWorkDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // Get past works for a specific household
    public CustomList<PastTask> getPastWorksByHousehold(String householdId) throws SQLException {
        String sql = "SELECT * FROM pastwork WHERE household_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, householdId);
            ResultSet rs = stmt.executeQuery();
            CustomList<PastTask> list = new CustomList<>();
            while (rs.next()) {
                list.add(new PastTask(
                        rs.getString("title"),
                        rs.getInt("task_id"),
                        rs.getString("worker_id"),
                        rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null,
                        rs.getInt("rating"),
                        rs.getString("review")
                ));
//            System.out.println(list.get(0));
            }
            return list;
        }
    }

    // Get past works for a specific worker
    public CustomList<PastTask> getPastWorksByWorker(String workerId) throws SQLException {
        String sql = "SELECT * FROM pastwork WHERE worker_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            System.out.println("successfully execute");
            stmt.setString(1, workerId);
            ResultSet rs = stmt.executeQuery();
            CustomList<PastTask> list = new CustomList<>();
            while (rs.next()) {
//                System.out.println(rs.getInt("worker_Id"));
                list.add(new PastTask(
                        rs.getInt("task_id"),
                        rs.getString("title"),
                        rs.getString("household_id"),
                        rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null,
                        rs.getInt("rating"),
                        rs.getString("review")
                ));
//            System.out.println(list.get(0));
            }
            return list;
        }
    }
}
