package app.household;

import database.household.OngoingWorkDAO;
import database.household.OngoingWorkDAOImpl;
import datastructures.CustomList;
import model.household.OngoingWork;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class OngoingWorkService {

    public CustomList<OngoingWork> getOngoingWorksForUser(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            OngoingWorkDAO dao = new OngoingWorkDAOImpl(conn);
            return dao.getOngoingWorksForUser(username);
        }
    }

    public CustomList<OngoingWork> getAllOngoingWorkers(String requestId){
        try (Connection conn = DatabaseConnection.getConnection()) {
            OngoingWorkDAO dao = new OngoingWorkDAOImpl(conn);
            return dao.getAllOngoingWorks(requestId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hireWorker(int requestId, String workerId, String startTime, String endTime, double expectedCost) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            OngoingWorkDAO dao = new OngoingWorkDAOImpl(conn);
            return dao.hireWorker(requestId, workerId, startTime, endTime, expectedCost);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
