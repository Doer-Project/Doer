package app;

import database.FutureWorkDAO;
import database.FutureWorkDAOImpl;
import model.FutureWork;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FutureWorkService {

    // Get future works for a specific household
    public List<FutureWork> getFutureWorksForHousehold(int householdId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            FutureWorkDAO dao = new FutureWorkDAOImpl(conn);
            return dao.getFutureWorkByHousehold(householdId);
        }
    }

    // Get upcoming jobs for a specific worker
    public List<FutureWork> getFutureWorksForWorker(int workerId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            FutureWorkDAO dao = new FutureWorkDAOImpl(conn);
            return dao.getUpcomingJobsByWorker(workerId);
        }
    }
}
