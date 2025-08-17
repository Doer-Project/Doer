package database.household;

import model.household.FutureWork;

import java.util.List;

public interface FutureWorkDAO {
    List<FutureWork> getFutureWorkForUser(String username);
}
