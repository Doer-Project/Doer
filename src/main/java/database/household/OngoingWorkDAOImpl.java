package database.household;

import model.household.OngoingWork;

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
        String sql = "SELECT title, description, preferred_work_date FROM workrequests WHERE household_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            /// checking
//        stmt.setInt(1,2);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ongoingWorks.add(new OngoingWork(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("preferred_work_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ongoingWorks;
    }
}
