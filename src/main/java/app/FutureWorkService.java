package app;

import database.FutureWorkDAO;
import database.FutureWorkDAOImpl;
import datastructures.CustomList;
import model.FutureWork;
import util.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class FutureWorkService {

    // Get future works for a specific household
    public CustomList<FutureWork> getFutureWorksForHousehold(String householdId){
            FutureWorkDAO dao = new FutureWorkDAOImpl();
            return dao.getFutureWorkByHousehold(householdId);
    }

    // Get upcoming jobs for a specific worker
    public CustomList<FutureWork> getFutureWorksForWorker(String workerId){
            FutureWorkDAO dao = new FutureWorkDAOImpl();
            return dao.getUpcomingJobsByWorker(workerId);
    }

    // Rate a completed job and mark it as rated
    public boolean rateWork(int taskId, int rating, String review){
            FutureWorkDAO dao = new FutureWorkDAOImpl();
            return dao.rateWork(taskId, rating, review);
    }
}
