package database;

import datastructures.CustomList;
import model.FutureWork;

public interface FutureWorkDAO {

    // Fetch future work for a household
    CustomList<FutureWork> getFutureWorkByHousehold(String householdId);

    // Fetch upcoming jobs for a worker
    CustomList<FutureWork> getUpcomingJobsByWorker(String workerId);

    // Mark a job as rated by household and store rating/review
    boolean rateWork(int taskId, int rating, String review);
}
