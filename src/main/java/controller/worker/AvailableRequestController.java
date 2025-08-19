package controller.worker;

import app.worker.AvailableWorkService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.worker.AvailableWork;
import model.SessionManager;
import util.MessageBox;

import java.sql.SQLException;
import java.util.List;

public class AvailableRequestController {

    @FXML
    private VBox container;

    @FXML
    public void initialize() {
        AvailableWorkService service = new AvailableWorkService();
        String userID = SessionManager.getUserID();

        List<AvailableWork> requests = service.getAvailableRequests(userID);
        container.getChildren().clear();

        if (requests.isEmpty()) {
            showNoRequestsMessage();
        } else {
            boolean toggleColor = true;
            for (AvailableWork req : requests) {
                HBox card = createWorkCard(req, toggleColor);
                container.getChildren().add(card);
                toggleColor = !toggleColor;
            }
        }
    }

    private void showNoRequestsMessage() {
        container.getChildren().clear();
        Label noDataLabel = new Label("No available requests");
        noDataLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #777;");
        noDataLabel.setAlignment(Pos.CENTER);
        noDataLabel.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(noDataLabel, Priority.ALWAYS);
        container.setAlignment(Pos.CENTER);
        container.getChildren().add(noDataLabel);
    }

    private HBox createWorkCard(AvailableWork work, boolean alternate) {
        HBox cardContainer = new HBox(20);
        cardContainer.setPadding(new Insets(15));

        // Alternating background
        String bgColor = alternate ? "#e0f7fa" : "#ffffff";
        cardContainer.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8,0,0,2);");

        // LEFT COLUMN
        VBox leftBox = new VBox(6);
        leftBox.setPrefWidth(300);
        leftBox.getChildren().addAll(
                createStyledLabel("👤 " + work.getHouseholdName()),
                createStyledLabel("🛠 " + work.getTitle()),
                createStyledLabel("📄 " + work.getDescription())
        );

        // RIGHT COLUMN
        VBox rightBox = new VBox(6);
        rightBox.setPrefWidth(300);
        rightBox.getChildren().addAll(
                createStyledLabel("📅 " + work.getDate()),
                createStyledLabel("📍 " + work.getAddress()),
                createStyledLabel("🏷 " + work.getPincode())
        );

        HBox detailsBox = new HBox(40, leftBox, rightBox);
        HBox.setHgrow(detailsBox, Priority.ALWAYS);

        // BUTTONS
        Button interestedBtn = new Button("Interested");
        interestedBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 8;");
        interestedBtn.setOnAction(e -> showInterestPopup(work));

        Button notInterestedBtn = new Button("Not Interested");
        notInterestedBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 8;");
        notInterestedBtn.setOnAction(e -> container.getChildren().remove(cardContainer));

        VBox btnBox = new VBox(10, interestedBtn, notInterestedBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        cardContainer.getChildren().addAll(detailsBox, btnBox);
        return cardContainer;
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        return label;
    }

    private void showInterestPopup(AvailableWork work) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Interested in Work");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label details = new Label(
                "Household: " + work.getHouseholdName() + "\n" +
                        "Title: " + work.getTitle() + "\n" +
                        "Description: " + work.getDescription() + "\n" +
                        "Date: " + work.getDate() + "\n" +
                        "Address: " + work.getAddress() + "\n" +
                        "Pincode: " + work.getPincode()
        );
        details.setStyle("-fx-font-size: 13px; -fx-text-fill: #222;");

        TextField chargesField = new TextField("Enter your charges");
        TextField timeField = new TextField("Expected completion time");
        TextField notesField = new TextField("Additional notes");

        content.getChildren().addAll(details, new Label("Provide your details:"), chargesField, timeField, notesField);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                MessageBox.showInfo("Submitted", "You expressed interest in: " + work.getTitle()
                        + "\nCharges: " + chargesField.getText()
                        + "\nTime: " + timeField.getText()
                        + "\nNotes: " + notesField.getText());
            }
            return null;
        });

        dialog.showAndWait();
    }
}