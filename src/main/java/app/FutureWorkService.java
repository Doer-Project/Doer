package app;

import database.FutureWorkDAO;
import database.FutureWorkDAOImpl;
import datastructures.CustomList;
import model.FutureWork;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FutureWorkService {

    // Get future works for a specific household
    public CustomList<FutureWork> getFutureWorksForHousehold(String householdId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            FutureWorkDAO dao = new FutureWorkDAOImpl(conn);
            return dao.getFutureWorkByHousehold(householdId);
        }
    }

    // Get upcoming jobs for a specific worker
    public CustomList<FutureWork> getFutureWorksForWorker(String workerId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            FutureWorkDAO dao = new FutureWorkDAOImpl(conn);
            return dao.getUpcomingJobsByWorker(workerId);
        }
    }
}
