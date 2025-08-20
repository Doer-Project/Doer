package app.worker;

import database.worker.AvailableRequestDAO;
import database.worker.AvailableRequestImpl;
import model.worker.AvailableWork;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AvailableWorkService {

    ///  just structure
    public List<AvailableWork> getAvailableRequests(String userID) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            AvailableRequestDAO dao = new AvailableRequestImpl(conn);
            return dao.getAvailableRequests(userID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
