package controller.household;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class HouseholdDBController {

    @FXML private Button btnNewRequest;
    @FXML private Button btnOngoing;
    @FXML private Button btnFuture;
    @FXML private Button btnCompleted;
    @FXML private Button btnProfile;

    @FXML private StackPane contentPane;

    @FXML
    public void createNewRequest() {
        resetButtonStyles();
        btnNewRequest.setStyle(activeStyle());
        loadUI("createRequest.fxml");
    }

    @FXML
    public void onGoingWork() {
        resetButtonStyles();
        btnOngoing.setStyle(activeStyle());
        loadUI("onGoing.fxml");
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
        loadUI("profile.fxml");
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
    }

    private String activeStyle() {
        return "-fx-background-color: #007bff;-fx-background-radius: 12; -fx-text-fill: white;";
    }
}
