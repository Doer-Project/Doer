package controller.worker;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WorkerInboxController {

    @FXML
    private VBox messagesContainer;

    @FXML
    public void initialize() {
        // Load worker-specific messages
        String[] messages = {
                "📥 Household X sent you a new work request",
                "📥 Household Y updated the schedule",
                "📥 Household Z accepted your quote"
        };

        for (String msg : messages) {
            Label label = new Label(msg);
            label.setStyle("-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: #F9F9F9; -fx-background-radius: 8;");
            messagesContainer.getChildren().add(label);
        }
    }
}
