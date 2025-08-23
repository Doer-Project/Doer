package app.household;

import database.household.OngoingWorkDAO;
import database.household.OngoingWorkDAOImpl;
import datastructures.CustomList;
import model.household.OngoingWork;
import java.sql.SQLException;

public class OngoingWorkService {

    public CustomList<OngoingWork> getOngoingWorksForUser(String username) throws SQLException {
        OngoingWorkDAO dao = new OngoingWorkDAOImpl();
        return dao.getOngoingWorksForUser(username);
    }

    public CustomList<OngoingWork> getAllOngoingWorkers(String requestId){
        OngoingWorkDAO dao = new OngoingWorkDAOImpl();
        return dao.getAllOngoingWorks(requestId);
    }

    public boolean hireWorker(int requestId, String workerId, String startTime, String endTime, double expectedCost) {
        OngoingWorkDAO dao = new OngoingWorkDAOImpl();
        return dao.hireWorker(requestId, workerId, startTime, endTime, expectedCost);
    }
}
