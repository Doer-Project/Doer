package database;

import model.PastTask;

import java.sql.SQLException;
import java.util.List;

public interface PastWorkDAO {
    public List<PastTask> getPastWorksByHousehold(String householdId) throws SQLException;

    public List<PastTask> getPastWorksByWorker(String workerId) throws  SQLException;
}