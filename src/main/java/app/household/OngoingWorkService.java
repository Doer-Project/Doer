package app.household;

import database.household.OngoingWorkDAO;
import database.household.OngoingWorkDAOImpl;
import model.household.OngoingWork;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OngoingWorkService {

    public List<OngoingWork> getOngoingWorksForUser(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
//            System.out.println("inside sevice");
            OngoingWorkDAO dao = new OngoingWorkDAOImpl(conn);
            return dao.getOngoingWorksForUser(username);
        }
    }

    public List<OngoingWork> getAllOngoingWorkers(String requestId){
        try (Connection conn = DatabaseConnection.getConnection()) {
            OngoingWorkDAO dao = new OngoingWorkDAOImpl(conn);
            return dao.getAllOngoingWorks(requestId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
