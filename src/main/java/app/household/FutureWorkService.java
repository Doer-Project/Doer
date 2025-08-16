package app.household;

import database.household.FutureWorkDAO;
import database.household.FutureWorkDAOImpl;
import model.FutureWork;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FutureWorkService {

    private FutureWorkDAO dao;

    ///  get connection and set to the FutureWorkDAOImpl
    public FutureWorkService() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        this.dao = new FutureWorkDAOImpl(conn);
    }

    public List<FutureWork> getFutureWorksForUser(String username){
        return dao.getFutureWorkForUser(username);
    }
}
