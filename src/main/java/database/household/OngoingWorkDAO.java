package database.household;

import model.household.OngoingWork;
import java.util.List;

public interface OngoingWorkDAO {
    List<OngoingWork> getOngoingWorksForUser(String username);
}
