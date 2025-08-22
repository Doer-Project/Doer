package database.household;

import datastructures.CustomList;
import model.household.OngoingWork;

public interface OngoingWorkDAO {
    CustomList<OngoingWork> getOngoingWorksForUser(String username);

    CustomList<OngoingWork> getAllOngoingWorks(String requestId);

    boolean hireWorker(int requestId, String workerId, String startTime, String endTime, double expectedCost);
}
