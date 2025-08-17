package database;

import model.Household;
import model.Worker;

import java.sql.SQLException;

public interface UserDAO {

    boolean registerHousehold(String userID, Household user) throws SQLException;

    boolean registerWorker(String userID, Worker user) throws SQLException;

    boolean verifyUser(String email, String password) throws SQLException;

    boolean isRegistered(String email) throws SQLException;

    String generateHouseholdId(Household user);

    String generateWorkerId(Worker user);

    String getSerial();

    String getUserIdByEmail(String email) throws SQLException;
}
