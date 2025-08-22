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
import java.sql.Time;
import java.time.LocalTime;
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
                createStyledLabel("🛠 " + work.getRequestTitle()), // updated for clarity
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
        interestedBtn.setOnAction(e -> showInterestPopup(work, cardContainer));

        Button notInterestedBtn = new Button("Not Interested");
        notInterestedBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 8;");
        notInterestedBtn.setOnAction(e -> showNotInterestedPopup(work, cardContainer));

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

    private void showInterestPopup(AvailableWork work, HBox cardContainer) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Interested in Work");

        VBox content = new VBox(15); // increased spacing
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Details section with styled background
        VBox detailsBox = new VBox(5);
        detailsBox.setPadding(new Insets(10));
        detailsBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5,0,0,1);");

        Label details = new Label(
                "Household: " + work.getHouseholdName() + "\n" +
                        "Title: " + work.getRequestTitle() + "\n" + // updated for clarity
                        "Description: " + work.getDescription() + "\n" +
                        "Date: " + work.getDate() + "\n" +
                        "Address: " + work.getAddress() + "\n" +
                        "Pincode: " + work.getPincode()
        );
        details.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
        details.setWrapText(true);

        detailsBox.getChildren().add(details);

        // Charges input
        TextField chargesField = new TextField();
        chargesField.setPromptText("Enter your expected charges");
        chargesField.setStyle("-fx-font-size: 13px; -fx-background-radius: 5; -fx-border-radius: 5;");

        // Start Time Spinners
        Spinner<Integer> startHour = new Spinner<>(0, 23, 9);
        Spinner<Integer> startMinute = new Spinner<>(0, 59, 0);
        HBox startTimeBox = new HBox(5, new Label("Start Time:"), startHour, new Label(":"), startMinute);
        startTimeBox.setAlignment(Pos.CENTER_LEFT);

        // End Time Spinners
        Spinner<Integer> endHour = new Spinner<>(0, 23, 17);
        Spinner<Integer> endMinute = new Spinner<>(0, 59, 0);
        HBox endTimeBox = new HBox(5, new Label("End Time:"), endHour, new Label(":"), endMinute);
        endTimeBox.setAlignment(Pos.CENTER_LEFT);

        // Add everything to main content
        content.getChildren().addAll(detailsBox, new Label("Provide your details:"), chargesField, startTimeBox, endTimeBox);

        // Dialog configuration
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);
        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    double estimatedCost = Double.parseDouble(chargesField.getText());
                    java.sql.Time startTime = java.sql.Time.valueOf(LocalTime.of(startHour.getValue(), startMinute.getValue()));
                    java.sql.Time endTime = java.sql.Time.valueOf(LocalTime.of(endHour.getValue(), endMinute.getValue()));
                    String userId = model.SessionManager.getUserID();
                    String requestId = work.getRequestId();
                    boolean success = new AvailableWorkService().markAsInterested(requestId, userId, startTime, endTime, estimatedCost);
                    if (success) {
                        container.getChildren().remove(cardContainer);
                        MessageBox.showInfo("Success", "Marked as interested!");
                    } else {
                        MessageBox.showError("Error", "Failed to update interest status.");
                    }
                } catch (Exception ex) {
                    MessageBox.showError("Input Error", "Please enter valid data.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showNotInterestedPopup(AvailableWork work, HBox cardContainer) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Not Interested in Work");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 10; -fx-background-radius: 10;");
        VBox detailsBox = new VBox(5);
        detailsBox.setPadding(new Insets(10));
        detailsBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5,0,0,1);");
        Label details = new Label(
                "Household: " + work.getHouseholdName() + "\n" +
                        "Title: " + work.getRequestTitle() + "\n" +
                        "Description: " + work.getDescription() + "\n" +
                        "Date: " + work.getDate() + "\n" +
                        "Address: " + work.getAddress() + "\n" +
                        "Pincode: " + work.getPincode()
        );
        details.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
        details.setWrapText(true);
        detailsBox.getChildren().add(details);
        Label confirmMsg = new Label("Are you sure you want to mark this request as 'Not Interested'?\nYou will not be able to apply for this request again.");
        confirmMsg.setStyle("-fx-font-size: 14px; -fx-text-fill: #b71c1c; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        content.getChildren().addAll(detailsBox, confirmMsg);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);
        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                String userId = model.SessionManager.getUserID();
                String requestId = work.getRequestId();
                boolean success = new AvailableWorkService().markAsNotInterested(requestId, userId);
                if (success) {
                    container.getChildren().remove(cardContainer);
                    MessageBox.showInfo("Success", "Marked as not interested.");
                } else {
                    MessageBox.showError("Error", "Failed to update status.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
}