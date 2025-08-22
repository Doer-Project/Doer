package database.household;

import model.household.OngoingWork;
import java.util.List;

public interface OngoingWorkDAO {
    List<OngoingWork> getOngoingWorksForUser(String username);

    List<OngoingWork> getAllOngoingWorks(String requestId);

    boolean hireWorker(int requestId, String workerId, String startTime, String endTime, double expectedCost);
}
