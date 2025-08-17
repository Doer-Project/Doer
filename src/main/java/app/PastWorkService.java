package app;

import database.PastWorkDAO;
import model.PastTask;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import database.PastWorkDAOImpl;
import util.MessageBox;

public class PastWorkService {

    public List<PastTask> getPastWorksForHousehold(int householdId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PastWorkDAO dao = new PastWorkDAOImpl(conn);
            return dao.getPastWorksByHousehold(householdId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            MessageBox.showError("Connection Failed", "Database not connected successfully");
            return null;
        }
    }

    public List<PastTask> getPastWorksForWorker(int workerId) {
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
