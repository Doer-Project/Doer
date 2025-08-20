package controller.household;

import app.MessageService;
import datastructures.CustomStack;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.SessionManager;

public class HouseholdInboxController {

    @FXML
    private VBox messagesContainer;

    private MessageService messageService = new MessageService();
    private String currentUserId = SessionManager.getUserID();

    @FXML
    public void initialize() {
        CustomStack<String> messagesStack = messageService.getInboxMessages(currentUserId);

        // Use temporary array to reverse the stack for bottom-up display
        String[] temp = new String[messagesStack.size()];
        int index = messagesStack.size() - 1;
        while (!messagesStack.isEmpty()) {
            temp[index--] = messagesStack.pop();
        }

        // Add labels to VBox from bottom to top
        for (String msg : temp) {
            Label label = new Label(msg);
            label.setStyle("-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: #F9F9F9; -fx-background-radius: 8;");
            messagesContainer.getChildren().add(label);
        }
    }
}
