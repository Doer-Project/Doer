package app.worker;

import database.worker.AvailableRequestDAO;
import database.worker.AvailableRequestImpl;
import datastructures.CustomList;
import model.worker.AvailableWork;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class AvailableWorkService {

    public CustomList<AvailableWork> getAvailableRequests(String userID) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            AvailableRequestDAO dao = new AvailableRequestImpl(conn);
            return dao.getAvailableRequests(userID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean markAsInterested(String requestId, String workerId, java.sql.Time startTime, java.sql.Time endTime, double estimatedCost) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            AvailableRequestDAO dao = new AvailableRequestImpl(conn);
            return dao.markAsInterested(requestId, workerId, startTime, endTime, estimatedCost);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean markAsNotInterested(String requestId, String workerId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            AvailableRequestDAO dao = new AvailableRequestImpl(conn);
            return dao.markAsNotInterested(requestId, workerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
