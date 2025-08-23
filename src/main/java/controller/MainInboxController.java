package controller;

import controller.household.HouseholdInboxController;
import controller.worker.WorkerInboxController;
import app.MessageService;
import datastructures.CustomStack;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.SessionManager;

public class MainInboxController {

    @FXML
    private VBox messagesContainer;

    private final MessageService messageService = new MessageService();
    private String currentUserId = SessionManager.getUserID();

    private boolean isHousehold = true; // default view

    @FXML
    public void initialize() {
        // Load default inbox
        loadInbox();
    }

    public void switchToHousehold() {
        isHousehold = true;
        loadInbox();
    }

    public void switchToWorker() {
        isHousehold = false;
        loadInbox();
    }

    private void loadInbox() {
        messagesContainer.getChildren().clear();

        CustomStack<String> messagesStack;

        if (isHousehold) {
            messagesStack = new HouseholdInboxController().getMessages(currentUserId);
        } else {
            messagesStack = new WorkerInboxController().getMessages(currentUserId);
        }

        String[] temp = new String[messagesStack.size()];
        int index = messagesStack.size() - 1;
        while (!messagesStack.isEmpty()) {
            temp[index--] = messagesStack.pop();
        }

        for (String msg : temp) {
            Label label = new Label(msg);
            label.setStyle(isHousehold
                    ? "-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: linear-gradient(to bottom, #E8F5E9, #C8E6C9); -fx-background-radius: 8;"
                    : "-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: linear-gradient(to bottom, #E3F2FD, #BBDEFB); -fx-background-radius: 8;");
            messagesContainer.getChildren().add(label);
        }
    }
}
