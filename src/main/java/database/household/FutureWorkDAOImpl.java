package database.household;

import model.FutureWork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FutureWorkDAOImpl implements FutureWorkDAO {
    private Connection conn;
    public FutureWorkDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<FutureWork> getFutureWorkForUser(String username) {
        List<FutureWork> futureWorks = new ArrayList<>();
// ----------------   will be replace
        String sql = "SELECT category, scheduled_date, time, description, status FROM future_work WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FutureWork fw = new FutureWork(
                        rs.getString("category"),
                        rs.getString("scheduled_date"),
                        rs.getString("time"),
                        rs.getString("description"),
                        rs.getString("status")
                );
                futureWorks.add(fw);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
// ----------------   will be replace
        return futureWorks;
    }
}
