package app.worker;

import database.worker.UpcomingJobDAO;
import database.worker.UpcomingJobDAOImpl;
import model.worker.UpcomingJob;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UpcomingJobService {
    public List<UpcomingJob> getUpcomingJobsForWorker(String workerId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            UpcomingJobDAO dao = new UpcomingJobDAOImpl(conn);
            return dao.getUpcomingJobsForWorker(workerId);
        }
    }
}
