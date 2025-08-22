package app;

import database.PastWorkDAO;
import datastructures.CustomList;
import model.PastTask;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import database.PastWorkDAOImpl;
import util.MessageBox;

public class PastWorkService {

    public CustomList<PastTask> getPastWorksForHousehold(String householdId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PastWorkDAO dao = new PastWorkDAOImpl(conn);
            return dao.getPastWorksByHousehold(householdId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            MessageBox.showError("Connection Failed", "Database not connected successfully");
            return null;
        }
    }

    public CustomList<PastTask> getPastWorksForWorker(String workerId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
//            System.out.println("In service");
            PastWorkDAO dao = new PastWorkDAOImpl(conn);
            return dao.getPastWorksByWorker(workerId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            MessageBox.showError("Connection Failed", "Database not connected successfully");
            return null;
        }
    }
}
