package database;

import model.PastTask;

import java.sql.SQLException;
import java.util.List;

public interface PastWorkDAO {
    public List<PastTask> getPastWorksByHousehold(int householdId) throws SQLException;

    public List<PastTask> getPastWorksByWorker(int workerId) throws  SQLException;
}