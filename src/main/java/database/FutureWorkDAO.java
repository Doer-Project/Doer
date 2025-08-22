package database;

import datastructures.CustomList;
import model.FutureWork;
import java.sql.SQLException;

public interface FutureWorkDAO {

    // Fetch future work for a household
    CustomList<FutureWork> getFutureWorkByHousehold(String householdId) throws SQLException;

    // Fetch upcoming jobs for a worker
    CustomList<FutureWork> getUpcomingJobsByWorker(String workerId) throws SQLException;
}
