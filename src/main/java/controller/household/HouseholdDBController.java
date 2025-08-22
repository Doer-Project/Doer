package controller.household;

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

public class HouseholdDBController {

    @FXML private Button btnNewRequest;
    @FXML private Button btnOngoing;
    @FXML private Button btnFuture;
    @FXML private Button btnCompleted;
    @FXML private Button btnProfile;
    @FXML private Button btnInbox;

    @FXML private StackPane contentPane;

    @FXML
    public void createNewRequest() {
        resetButtonStyles();
        btnNewRequest.setStyle(activeStyle());
        loadUI("CreateRequest.fxml");
    }

    @FXML
    public void onGoingWork() {
        resetButtonStyles();
        btnOngoing.setStyle(activeStyle());
        loadUI("OnGoing.fxml");
    }

    @FXML
    public void futureWork() {
        resetButtonStyles();
        btnFuture.setStyle(activeStyle());
        loadUI("FutureWork.fxml");
    }

    @FXML
    public void completedWork() {
        resetButtonStyles();
        btnCompleted.setStyle(activeStyle());
        loadUI("Completed.fxml");
    }

    @FXML
    public void viewProfile() {
        resetButtonStyles();
        btnProfile.setStyle(activeStyle());
        Node node = null;
        try {
            node = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        contentPane.getChildren().setAll(node);
    }

    @FXML
    private void openInbox() {
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
            Node node = FXMLLoader.load(getClass().getResource("/fxml/household/" + fxmlFile));
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            System.out.println("Error loading UI: " + fxmlFile);
            e.printStackTrace();
        }
    }


    public void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-text-fill: #1B4242;";
        btnNewRequest.setStyle(defaultStyle);
        btnOngoing.setStyle(defaultStyle);
        btnFuture.setStyle(defaultStyle);
        btnCompleted.setStyle(defaultStyle);
        btnProfile.setStyle(defaultStyle);
        btnInbox.setStyle(defaultStyle);
    }

    private String activeStyle() {
        return "-fx-background-color: #007bff;-fx-background-radius: 12; -fx-text-fill: white;";
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
