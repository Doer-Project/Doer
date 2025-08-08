package database.household;

import model.OngoingWork;
import java.util.List;

public interface OngoingWorkDAO {
    List<OngoingWork> getOngoingWorksForUser(String username);
}
