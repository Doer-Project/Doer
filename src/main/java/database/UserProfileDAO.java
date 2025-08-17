package database;

import java.sql.SQLException;
import java.util.List;

public interface UserProfileDAO {
    /**
     * Fetch user profile data by userId
     * Order: [first_name, last_name, email, address, user_type, gender, profile_pic]
     */
    List<String> fetchUserDataList(String userId) throws SQLException;
}
