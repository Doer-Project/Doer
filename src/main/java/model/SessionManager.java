package model;

public class SessionManager {
    private static String userID;

    public static void setUserID(String userID) {
        SessionManager.userID = userID;
    }

    public static String getUserID() {
        return userID;
    }
}