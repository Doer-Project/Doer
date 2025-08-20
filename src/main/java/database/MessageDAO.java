package database;

import datastructures.CustomStack;

import java.util.Stack;

public interface MessageDAO {
    CustomStack<String> getMessagesForUser(String userId);
    public boolean insertMessage(String receiverId, String senderName, String message);
}
