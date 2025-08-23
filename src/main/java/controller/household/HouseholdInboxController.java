package controller.household;

import app.MessageService;
import datastructures.CustomStack;

public class HouseholdInboxController {

    private final MessageService messageService = new MessageService();

    public CustomStack<String> getMessages(String userId) {
        return messageService.getInboxMessages(userId);
    }
}
