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
    public List<PastTask> getPastWorksByHousehold(int householdId) throws SQLException {
        String sql = "SELECT * FROM pastwork WHERE household_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, householdId);
            ResultSet rs = stmt.executeQuery();
            return mapResultSet(rs);
        }
    }

    // Get past works for a specific worker
    public List<PastTask> getPastWorksByWorker(int workerId) throws SQLException {
        String sql = "SELECT * FROM pastwork WHERE worker_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, workerId);
            ResultSet rs = stmt.executeQuery();
            return mapResultSet(rs);
        }
    }

    private List<PastTask> mapResultSet(ResultSet rs) throws SQLException {
        List<PastTask> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new PastTask(
                    rs.getInt("past_id"),
                    rs.getInt("task_id"),
                    rs.getObject("worker_id", Integer.class),
                    rs.getObject("household_id", Integer.class),
                    rs.getString("title"),
                    rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null,
                    rs.getObject("rating", Integer.class),
                    rs.getString("review")
            ));
        }
        return list;
    }
}
