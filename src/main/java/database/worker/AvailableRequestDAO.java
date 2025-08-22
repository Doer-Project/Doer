package database.worker;

import model.household.OngoingWork;
import model.worker.AvailableWork;

import java.util.List;

public interface AvailableRequestDAO {
    List<AvailableWork> getAvailableRequests(String username);
    boolean markAsInterested(String requestId, String workerId, java.sql.Time startTime, java.sql.Time endTime, double estimatedCost);
    boolean markAsNotInterested(String requestId, String workerId);
}
