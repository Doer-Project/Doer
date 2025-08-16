package database.household;

import model.WorkRequest;

public interface WorkReqDAO {
    public boolean insert(WorkRequest request);
}
