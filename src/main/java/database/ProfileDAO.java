package database;

import model.User;

import java.sql.SQLException;

public interface ProfileDAO {
    boolean updateUser(User user, boolean confirm) throws SQLException;

    User getUserByUsername(String username) throws SQLException;
}

