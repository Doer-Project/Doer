package database.worker;

import datastructures.CustomList;
import model.household.OngoingWork;
import model.worker.AvailableWork;

import java.util.List;

public interface AvailableRequestDAO {
    CustomList<AvailableWork> getAvailableRequests(String username);
    boolean markAsInterested(String requestId, String workerId, java.sql.Time startTime, java.sql.Time endTime, double estimatedCost);
    boolean markAsNotInterested(String requestId, String workerId);
}
