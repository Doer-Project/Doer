package controller.worker;

import app.MessageService;
import datastructures.CustomStack;

public class WorkerInboxController {

    private final MessageService messageService = new MessageService();

    public CustomStack<String> getMessages(String userId) {
        return messageService.getInboxMessages(userId);
    }
}
