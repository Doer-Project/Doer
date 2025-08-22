package database;

import model.FutureWork;
import java.sql.SQLException;
import java.util.List;

public interface FutureWorkDAO {

    // Fetch future work for a household
    List<FutureWork> getFutureWorkByHousehold(String householdId) throws SQLException;

    // Fetch upcoming jobs for a worker
    List<FutureWork> getUpcomingJobsByWorker(String workerId) throws SQLException;
}
