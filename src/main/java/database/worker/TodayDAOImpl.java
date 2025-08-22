package database.worker;

import datastructures.CustomList;
import model.FutureWork;
import util.DBConnection;

import java.sql.*;

public class TodayDAOImpl implements TodayDAO {

    public CustomList<FutureWork> fetchOngoingJobsForToday() {
        CustomList<FutureWork> jobs = new CustomList<>();
        String query = "SELECT task_id, household_id, title, address, start_time, end_time, cost " +
                "FROM futurework WHERE status = 'upcoming' AND date = CURDATE()";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println(rs.getString("household_id"));
                jobs.add(new FutureWork(
                        rs.getInt("task_id"),
                        rs.getString("household_id"),
                        rs.getString("title"),
                        rs.getString("address"),
                        null,
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getString("cost"),
                        null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public boolean updateJobStatusToComplete(int jobId) {
        String query = "UPDATE assignedtasks SET status = 'COMPLETED' WHERE task_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, jobId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
