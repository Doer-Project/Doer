package database.household;

import model.household.WorkRequest;

public interface WorkReqDAO {
    public boolean insert(WorkRequest request);
}
