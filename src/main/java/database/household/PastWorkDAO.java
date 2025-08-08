package database.household;

import model.PastWork;

import java.util.List;

public interface PastWorkDAO {
     List<PastWork> getPastWorkForUser(String username);
}
