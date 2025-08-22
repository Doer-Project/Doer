package controller.worker;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WorkerDBController {

    @FXML private Button btnAvailableRequests;
    @FXML private Button btnTodaySchedule;
    @FXML private Button btnUpcomingTasks;
    @FXML private Button btnPastWork;
    @FXML private Button btnProfile;
    @FXML private Button btnInbox;

    @FXML private StackPane contentPane;

    /// have to change
    @FXML
    public void availableRequest() {
        resetButtonStyles();
        btnAvailableRequests.setStyle(activeStyle());
        loadUI("Available_req.fxml");
    }

    // DUMMY - KEEP AS IS
    public void todaySchedule(ActionEvent event) {
        resetButtonStyles();
        btnTodaySchedule.setStyle(activeStyle());
        loadUI("TodaySchedule.fxml");
    }

    // ✅ REAL - SHOW UPCOMING
    public void upcomingTask(ActionEvent event) {
        resetButtonStyles();
        btnUpcomingTasks.setStyle(activeStyle());
        System.out.println("Trying to load future.fxml...");
        loadUI("UpcomingJob.fxml");
    }


    // ✅ REAL - SHOW PAST WORK
    public void pastWork(ActionEvent event) {
        resetButtonStyles();
        btnPastWork.setStyle(activeStyle());
        loadUI("CompletedJobs.fxml");
    }

    // DUMMY - KEEP AS IS
    public void profile(ActionEvent event) {
        resetButtonStyles();
        btnProfile.setStyle(activeStyle());
        try {
            Node node = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            System.out.println("Error loading UI: Profile.fxml");
            e.printStackTrace();
        }
    }

    @FXML
    private void openInbox(ActionEvent event) {
        resetButtonStyles();
        btnInbox.setStyle(activeStyle());
        Node node = null;
        try {
            node = FXMLLoader.load(getClass().getResource("/fxml/Inbox.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        contentPane.getChildren().setAll(node);
    }


    private void loadUI(String fxmlFile) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/fxml/worker/"+fxmlFile));
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            System.out.println("Error loading UI: " + fxmlFile);
            e.printStackTrace();
        }
    }

    public void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-text-fill: #1B4242;";
        btnAvailableRequests.setStyle(defaultStyle);
        btnTodaySchedule.setStyle(defaultStyle);
        btnUpcomingTasks.setStyle(defaultStyle);
        btnPastWork.setStyle(defaultStyle);
        btnProfile.setStyle(defaultStyle);
        btnInbox.setStyle(defaultStyle);
    }

    private String activeStyle() {
        return "-fx-background-color: #007bff; -fx-background-radius: 12; -fx-text-fill: white;";
    }

    public void logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginPage.fxml"));
                Scene scene = new Scene(root);

                // Get current stage and set login scene
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Login - DOER");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

