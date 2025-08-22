package database.worker;

import datastructures.CustomList;
import model.FutureWork;

public interface TodayDAO {
    public CustomList<FutureWork> fetchOngoingJobsForToday();

    boolean updateJobStatusToComplete(int jobId);
}
