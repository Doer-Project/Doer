package database.worker;

import model.worker.UpcomingJob;

import java.util.List;

public interface UpcomingJobDAO {

    public List<UpcomingJob> getUpcomingJobsForWorker(String workerId);
}
