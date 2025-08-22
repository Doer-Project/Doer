package database;

import model.PastTask;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PastWorkDAOImpl implements PastWorkDAO {
    private final Connection conn;

    public PastWorkDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // Get past works for a specific household
    public List<PastTask> getPastWorksByHousehold(String householdId) throws SQLException {
        String sql = "SELECT * FROM pastwork WHERE household_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, householdId);
            ResultSet rs = stmt.executeQuery();
            List<PastTask> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new PastTask(
                        rs.getInt("task_id"),
                        rs.getString("title"),
                        rs.getObject("worker_id", Integer.class),
                        rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null,
                        rs.getObject("rating", Integer.class),
                        rs.getString("review")
                ));
//            System.out.println(list.get(0));
            }
            return list;
        }
    }

    // Get past works for a specific worker
    public List<PastTask> getPastWorksByWorker(String workerId) throws SQLException {
        String sql = "SELECT * FROM pastwork WHERE worker_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            System.out.println("successfully execute");
            stmt.setString(1, workerId);
            ResultSet rs = stmt.executeQuery();
            List<PastTask> list = new ArrayList<>();
            while (rs.next()) {
//                System.out.println(rs.getInt("worker_Id"));
                list.add(new PastTask(
                        rs.getInt("task_id"),
                        rs.getObject("household_id", Integer.class),
                        rs.getString("title"),
                        rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null,
                        rs.getObject("rating", Integer.class),
                        rs.getString("review")
                ));
//            System.out.println(list.get(0));
            }
            return list;
        }
    }
}
