package controller.household;

import app.household.OngoingWorkService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.household.OngoingWork;
import model.SessionManager;
import util.MessageBox;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HouseholdOngoingController {
    @FXML
    private StackPane rootStack; // root of FXML

    @FXML
    private VBox container;

    public void initialize() {
        try {
//            System.out.println("inside controller");
            ///  object for calling method like getOngoingWorksForUser(username)
            OngoingWorkService service = new OngoingWorkService();
            String username = SessionManager.getUserID();

            List<OngoingWork> ongoingWorks = service.getOngoingWorksForUser(username);
            ///  it is for to delete the previous ui element;
            container.getChildren().clear();
            ongoingWorks.add(new OngoingWork("bla","aa", "aa"));

            if (ongoingWorks.isEmpty()) {
                /// ui can change later, this is temporary ui
                showNoRequestsMessage();
            } else {
                /// if data is available for ongoing work then pass the list to another method
                loadOngoingRequests(ongoingWorks);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MessageBox.showError("Error Loading Data", "Failed to load ongoing work. Please try again later.");
        }
    }

    private void loadOngoingRequests(List<OngoingWork> ongoingWorks) {
        container.setAlignment(Pos.TOP_LEFT);  // Reset alignment for list
        for (int i = 0; i < ongoingWorks.size(); i++) {
            OngoingWork work = ongoingWorks.get(i);

            // LEFT side of the label, where basic information show of the work.
            // every child in this box have space of 8 between each other
            VBox leftBox = new VBox(8);
            Label categoryLabel = new Label("Category: " + work.getTaskName());
            Label dateLabel = new Label("Date: " + work.getDate());
            Label descLabel = new Label("Description: " + work.getDescription());
            descLabel.setWrapText(true);
            descLabel.setMaxWidth(600);
            leftBox.getChildren().addAll(categoryLabel, dateLabel, descLabel);

            // RIGHT BOX: Status and button(View details)
            // every child in this box have space of 10 between each other
            VBox rightBox = new VBox(10);
            rightBox.setAlignment(Pos.TOP_RIGHT);
            Button viewDetailsButton = new Button("View Details");
            viewDetailsButton.setStyle("-fx-background-color: linear-gradient(to right, #FF9F57, #E8781C);\n" +
                    "                        -fx-text-fill: white;\n" +
                    "                        -fx-background-radius: 12;\n" +
                    "                        -fx-padding: 8 20;\n" +
                    "                        -fx-font-size: 14px;\n" +
                    "                        -fx-font-weight: bold;\n" +
                    "                        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0, 0, 2);");
            viewDetailsButton.setOnAction(e -> showDetailsPopup(work));
            rightBox.getChildren().add(viewDetailsButton);

            // Combine left and right boxes in an HBox
            // every child(LEFT & RIGHT box) in this box have space of 50 between each other
            HBox row = new HBox(50);
            row.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(leftBox, Priority.ALWAYS);
            row.getChildren().addAll(leftBox, rightBox);

            // Card container styling
            VBox card = new VBox(row);
            card.setSpacing(15);
            card.setPadding(new Insets(20));
            // alternative color for the ongoing labels.
            String bgColor = (i % 2 == 0) ? "white" : "#E0F7F1";
            card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 20; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);");

            container.getChildren().add(card);
        }
    }

    private void showNoRequestsMessage() {
        container.getChildren().clear();
        Label noDataLabel = new Label("No ongoing requests available");
        noDataLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #777;");
        noDataLabel.setAlignment(Pos.CENTER);
        noDataLabel.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(noDataLabel, Priority.ALWAYS);
        container.setAlignment(Pos.CENTER);
        container.getChildren().add(noDataLabel);
    }

    /// this 2 alerts can later add to MessageBox
    private void showDetailsPopup(OngoingWork work) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/household/OngoingDetails.fxml"));
            Parent detailsRoot = loader.load();

            // Pass selected work to the controller
            OngoingDetailsController controller = loader.getController();
            controller.setOngoingWork(work);

            // Create overlay pane
            StackPane overlay = new StackPane();
            overlay.setPrefSize(1000, 800);
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);"); // semi-transparent background
            overlay.getChildren().add(detailsRoot);
            StackPane.setAlignment(detailsRoot, Pos.CENTER);

            // Optional: add a close button
            Button closeBtn = new Button("X");
            closeBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
            closeBtn.setOnAction(e -> rootStack.getChildren().remove(overlay));
            StackPane.setAlignment(closeBtn, Pos.TOP_RIGHT);
            overlay.getChildren().add(closeBtn);

            // Add overlay to rootStack
            rootStack.getChildren().add(overlay);

        } catch (IOException e) {
            e.printStackTrace();
            MessageBox.showError("Error", "Failed to open details page.");
        }
    }

}
