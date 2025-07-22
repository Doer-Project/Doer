package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;

import java.io.IOException;

public class HouseholdDashboardController {

    @FXML
    private StackPane contentPane;

    @FXML
    public void createNewRequest(ActionEvent event) {
        loadUI("createRequest.fxml");
    }

    @FXML
    public void onGoingWork(ActionEvent event) {
        loadUI("onGoing.fxml");
    }

    @FXML
    public void futureWork(ActionEvent event) {
        loadUI("futureWork.fxml");
    }

    @FXML
    public void completedWork(ActionEvent event) {
        loadUI("completed.fxml");
    }

    @FXML
    public void viewProfile(ActionEvent event) {
        loadUI("profile.fxml");
    }

    private void loadUI(String fxmlFile) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/fxml/household/" + fxmlFile));
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
