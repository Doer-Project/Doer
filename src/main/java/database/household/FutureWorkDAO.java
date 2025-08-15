package database.household;

import model.FutureWork;

import java.sql.SQLException;
import java.util.List;

public interface FutureWorkDAO {
    List<FutureWork> getFutureWorkForUser(String username);
}
