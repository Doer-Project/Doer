package database.worker;

import model.household.OngoingWork;
import model.worker.AvailableWork;

import java.util.List;

public interface AvailableRequestDAO {
    List<AvailableWork> getAvailableRequests(String username);
}
