package controller.worker;

import app.MessageService;
import datastructures.CustomStack;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.SessionManager;

public class WorkerInboxController {

    @FXML
    private VBox messagesContainer;

    private MessageService messageService = new MessageService();
    private String currentUserId  = SessionManager.getUserID(); // replace with SessionManager.getWorkerId()

    @FXML
    public void initialize() {
        CustomStack<String> messagesStack = messageService.getInboxMessages(currentUserId);

        String[] temp = new String[messagesStack.size()];
        int index = messagesStack.size() - 1;
        while (!messagesStack.isEmpty()) {
            temp[index--] = messagesStack.pop();
        }

        for (String msg : temp) {
            Label label = new Label(msg);
            label.setStyle("-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: linear-gradient(to bottom, #E3F2FD, #BBDEFB); -fx-background-radius: 8;");
            messagesContainer.getChildren().add(label);
        }
    }
}
