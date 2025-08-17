package app.household;

import database.household.OngoingWorkDAO;
import database.household.OngoingWorkDAOImpl;
import model.OngoingWork;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OngoingWorkService {

    public List<OngoingWork> getOngoingWorksForUser(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            OngoingWorkDAO dao = new OngoingWorkDAOImpl(conn);
            return dao.getOngoingWorksForUser(username);
        }
    }
}
