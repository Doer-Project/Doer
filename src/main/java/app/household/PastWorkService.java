package app.household;

import database.household.PastWorkDAOImpl;
import model.PastWork;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PastWorkService {
    private PastWorkDAOImpl dao;
    public PastWorkService() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        dao = new PastWorkDAOImpl(conn);
    }

    public List<PastWork> getPastWorkForUser(String username) {
        return dao.getPastWorkForUser(username);
    }
}
