package app;

import database.MessageDAO;
import database.MessageDAOImpl;
import datastructures.CustomStack;

import java.util.Stack;

public class MessageService {

    private MessageDAO messageDAO = new MessageDAOImpl();

    // Fetch inbox messages for a user (household or worker) as a Stack
    public CustomStack<String> getInboxMessages(String userId) {
        return messageDAO.getMessagesForUser(userId);
    }

    // Send a message to a user (worker or household)
    public boolean sendMessage(String receiverId, String senderName, String message) {
        return messageDAO.insertMessage(receiverId, senderName, message);
    }
}
