package app;

import database.OngoingWorkDAOImpl;
import model.OngoingWork;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OngoingWorkService {
    private OngoingWorkDAOImpl dao;

    public OngoingWorkService() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        this.dao = new OngoingWorkDAOImpl(conn);
    }

    public List<OngoingWork> getOngoingWorksForUser(String username) {
        return dao.getOngoingWorksForUser(username);
    }
}