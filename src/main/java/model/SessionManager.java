package model;

public class SessionManager {
    // Static variable to hold username accessible everywhere
    public static String username;
    public static User currentUser;
    // Private constructor to prevent instantiation
    SessionManager(){ }
    /**
     * Set the current logged-in user
     * @param user The User object to set as current
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
        if (user != null) {
            username = user.getUserName();
        }
        currentUser = user;
    }
    /**
     * Get the current logged-in user
     * @return the User object
     */
    public static User getCurrentUser() {
        return currentUser;
    }
}
