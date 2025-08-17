package app.household;

import database.household.FutureWorkDAO;
import database.household.FutureWorkDAOImpl;
import model.FutureWork;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FutureWorkService {

    public List<FutureWork> getFutureWorksForUser(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            FutureWorkDAO dao = new FutureWorkDAOImpl(conn);
            return dao.getFutureWorkForUser(username);
        }
    }
}
