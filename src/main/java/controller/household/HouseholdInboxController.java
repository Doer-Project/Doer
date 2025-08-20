package controller.household;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HouseholdInboxController {

    @FXML
    private VBox messagesContainer;

    @FXML
    public void initialize() {
        // Load household-specific messages
        String[] messages = {
                "📥 Worker A accepted your request",
                "📥 Worker B completed your scheduled work",
                "📥 Worker C updated the schedule"
        };

        for (String msg : messages) {
            Label label = new Label(msg);
            label.setStyle("-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: #F9F9F9; -fx-background-radius: 8;");
            messagesContainer.getChildren().add(label);
        }
    }
}
