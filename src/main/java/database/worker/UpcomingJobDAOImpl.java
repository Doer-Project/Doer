package database.worker;

import model.worker.UpcomingJob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpcomingJobDAOImpl implements UpcomingJobDAO {

    private Connection conn;

    public UpcomingJobDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<UpcomingJob> getUpcomingJobsForWorker(String workerId) {
        List<UpcomingJob> jobs = new ArrayList<>();

        // ----------------   will be replaced
        String sql = "SELECT work_id, service, customer_name, scheduled_date, address " +
                "FROM upcoming_jobs " +
                "WHERE worker_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, workerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UpcomingJob job = new UpcomingJob(
                        rs.getString("work_id"),
                        rs.getString("service"),
                        rs.getString("customer_name"),
                        rs.getString("scheduled_date"),
                        rs.getString("address")
                );
                jobs.add(job);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // ----------------   will be replaced

        return jobs;
    }
}
