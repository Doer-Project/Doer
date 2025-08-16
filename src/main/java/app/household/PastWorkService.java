package app.household;

import database.household.PastWorkDAO;
import database.household.PastWorkDAOImpl;
import model.PastWork;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PastWorkService {

    public List<PastWork> getPastWorkForUser(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PastWorkDAO dao = new PastWorkDAOImpl(conn);
            return dao.getPastWorkForUser(username);
        }
    }
}
