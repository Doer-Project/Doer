package database;

import datastructures.CustomStack;


public interface MessageDAO {
    CustomStack<String> getMessagesForUser(String userId);
    public boolean insertMessage(String receiverId, String message);
}
