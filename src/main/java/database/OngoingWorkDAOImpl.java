package database;

import database.OngoingWorkDAO;
import model.OngoingWork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OngoingWorkDAOImpl implements OngoingWorkDAO {

    private Connection conn;

    public OngoingWorkDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<OngoingWork> getOngoingWorksForUser(String username) {

        /// temporary solution ⬇️, This is not final we have to change it.

        List<OngoingWork> ongoingWorks = new ArrayList<>();
        String sql = "SELECT task_name, description, date, status FROM requests WHERE household_username = ? AND status != 'Completed'";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ongoingWorks.add(new OngoingWork(
                        rs.getString("task_name"),
                        rs.getString("description"),
                        rs.getString("date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ongoingWorks;
    }
}
