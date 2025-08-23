package database.worker;

import datastructures.CustomList;
import model.FutureWork;
import util.DBConnection;

import java.sql.*;

public class TodayDAOImpl implements TodayDAO {
    private Connection connection;

    public TodayDAOImpl() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public CustomList<FutureWork> fetchOngoingJobsForToday() {
        CustomList<FutureWork> jobs = new CustomList<>();
        String query = "SELECT task_id, household_id, title, address, start_time, end_time, cost " +
                "FROM futurework WHERE status = 'upcoming' AND date = CURDATE()";

        try (Statement stmt = connection.createStatement();
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
        String updAssigned = "UPDATE assignedtasks SET status = 'completed', completed_at = NOW() WHERE task_id = ?";
        String updRequest = "UPDATE workrequests SET status = 'completed' WHERE request_id = (SELECT request_id FROM assignedtasks WHERE task_id = ?)";
        boolean oldAuto = false;
        try {
            oldAuto = connection.getAutoCommit();

            connection.setAutoCommit(false);
            try (PreparedStatement ps1 = connection.prepareStatement(updAssigned);
                 PreparedStatement ps2 = connection.prepareStatement(updRequest)) {
                ps1.setInt(1, jobId);
                int a = ps1.executeUpdate();
                ps2.setInt(1, jobId);
                ps2.executeUpdate();
                if (a == 0) {
                    connection.rollback();
                    return false;
                }
                connection.commit();
                return true;
            } catch (SQLException ex) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(oldAuto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
