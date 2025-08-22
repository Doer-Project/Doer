package database;

import datastructures.CustomList;
import model.PastTask;
import java.sql.SQLException;

public interface PastWorkDAO {
    public CustomList<PastTask> getPastWorksByHousehold(String householdId) throws SQLException;

    public CustomList<PastTask> getPastWorksByWorker(String workerId) throws  SQLException;
}