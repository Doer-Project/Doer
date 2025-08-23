package app.worker;

import database.worker.AvailableRequestDAO;
import database.worker.AvailableRequestImpl;
import datastructures.CustomList;
import model.worker.AvailableWork;

public class AvailableWorkService {

    ///  just structure
    public CustomList<AvailableWork> getAvailableRequests(String userID) {
        AvailableRequestDAO dao = new AvailableRequestImpl();
        return dao.getAvailableRequests(userID);
    }

    public boolean markAsInterested(String requestId, String workerId, java.sql.Time startTime, java.sql.Time endTime, double estimatedCost) {
        AvailableRequestDAO dao = new AvailableRequestImpl();
        return dao.markAsInterested(requestId, workerId, startTime, endTime, estimatedCost);
    }

    public boolean markAsNotInterested(String requestId, String workerId) {
        AvailableRequestDAO dao = new AvailableRequestImpl();
        return dao.markAsNotInterested(requestId, workerId);
    }
}
