package app.worker;

import database.worker.TodayDAO;
import database.worker.TodayDAOImpl;
import datastructures.CustomList;
import model.FutureWork;


public class TodayService {
    private final TodayDAO jobDAO = new TodayDAOImpl();

    public CustomList<FutureWork> getOngoingJobsForToday() {
        return jobDAO.fetchOngoingJobsForToday();
    }

    public boolean markJobAsComplete(int jobId) {
        return jobDAO.updateJobStatusToComplete(jobId);
    }
}
