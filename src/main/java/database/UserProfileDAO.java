package database;

import datastructures.CustomList;

import java.sql.SQLException;

public interface UserProfileDAO {
    /**
     * Fetch user profile data by userId
     * Order: [first_name, last_name, email, address, user_type, gender, profile_pic]
     */
    CustomList<String> fetchUserDataList(String userId) throws SQLException;
    boolean updateUserProfile(String userId, String firstName, String lastName,
                              String addressOrWorkArea, String userType) throws SQLException;

    /**
     * Fetch the profile picture as a byte array (BLOB) for the given userId
     */
    byte[] getProfilePicture(String userId) throws SQLException;

    /**
     * Update the profile picture (BLOB) for the given userId
     */
    boolean updateProfilePicture(String userId, byte[] imageBytes) throws SQLException;
}
