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
        String sql = "SELECT request_id, title, description, preferred_work_date FROM workrequests WHERE household_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            /// checking
//        stmt.setInt(1,2);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ongoingWorks.add(new OngoingWork(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("preferred_work_date"),
                        rs.getInt("request_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ongoingWorks;
    }

    @Override
    public List<OngoingWork> getAllOngoingWorks(String requestId) {
        List<OngoingWork> ongoingWorks = new ArrayList<>();
        String sql = "select worker_id, interest_status, proposed_start_time, proposed_end_time, estimated_cost from requestrecipients where request_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, requestId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String query = "SELECT first_name, last_name FROM users WHERE user_id = ?";
                PreparedStatement userStmt = conn.prepareStatement(query);
                String workerId = rs.getString("worker_id");
                userStmt.setString(1, workerId);
                ResultSet userRs = userStmt.executeQuery();
                String workerName = "";
                if (userRs.next()) {
                    workerName = userRs.getString("first_name") + " " + userRs.getString("last_name");
                }
                ongoingWorks.add(new OngoingWork(workerId,
                        workerName,
                        rs.getString("interest_status"),
                        rs.getString("proposed_start_time"),
                        rs.getString("proposed_end_time"),
                        rs.getDouble("estimated_cost")+""
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ongoingWorks;
    }
}
