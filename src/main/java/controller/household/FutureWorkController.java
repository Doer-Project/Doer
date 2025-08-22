package controller.household;

import app.FutureWorkService;
import datastructures.CustomList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.FutureWork;
import model.SessionManager;
import util.FXUtil;
import util.MessageBox;
import java.sql.Time;
import java.time.LocalDate;

public class FutureWorkController {

    @FXML private TableView<FutureWork> futureWorkTable;
    @FXML private TableColumn<FutureWork, Integer> colTaskId;
    @FXML private TableColumn<FutureWork, String> colTitle;
    @FXML private TableColumn<FutureWork, Integer> colWorkerId;
    @FXML private TableColumn<FutureWork, LocalDate> colDate;
    @FXML private TableColumn<FutureWork, Time> colStartTime;
    @FXML private TableColumn<FutureWork, Time> colEndTime;
    @FXML private TableColumn<FutureWork, Double> colCost;
    @FXML private TableColumn<FutureWork, String> colStatus;
    @FXML private TableColumn<FutureWork, Void> colAction;

    private FutureWorkService service;

    @FXML
    public void initialize() {
        try {
            System.out.println("controller");
            service = new FutureWorkService();

            colTaskId.setCellValueFactory(new PropertyValueFactory<>("taskId"));
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colWorkerId.setCellValueFactory(new PropertyValueFactory<>("workerId"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            // Action column with Confirm button shown only when status == 'complete'
            colAction.setCellFactory(col -> new TableCell<>() {
                private final Button confirmBtn = new Button("Confirm");
                {
                    confirmBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 6;");
                    confirmBtn.setOnAction(e -> {
                        FutureWork fw = getTableView().getItems().get(getIndex());
                        showRatingDialogAndConfirm(fw);
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        return;
                    }
                    FutureWork fw = getTableView().getItems().get(getIndex());
                    String status = fw.getStatus();
                    if (status != null && (status.equalsIgnoreCase("complete") || status.equalsIgnoreCase("completed"))) {
                        confirmBtn.setDisable(false);
                        setGraphic(confirmBtn);
                    } else {
                        setGraphic(null);
                    }
                }
            });

            loadData(SessionManager.getUserID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showRatingDialogAndConfirm(FutureWork fw) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Confirm and Rate Work");

        // Content
        VBox content = new VBox(12);
        content.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 16;");

        Label info = new Label("Please provide a rating (1-5). Review is optional.");
        info.setStyle("-fx-font-size: 13px;");

        ComboBox<Integer> ratingBox = new ComboBox<>();
        ratingBox.getItems().addAll(1, 2, 3, 4, 5);
        ratingBox.setPromptText("Select rating (1-5)");

        TextArea reviewArea = new TextArea();
        reviewArea.setPromptText("Write a short review (optional)");
        reviewArea.setPrefRowCount(3);

        content.getChildren().addAll(info, ratingBox, reviewArea);

        ButtonType okType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okType, cancelType);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(btn -> {
            if (btn == okType) {
                Integer rating = ratingBox.getValue();
                if (rating == null) {
                    MessageBox.showError("Validation", "Please select a rating between 1 and 5.");
                    return null;
                }
                String review = reviewArea.getText();
                try {
                    boolean success = service.rateWork(fw.getTaskId(), rating, review);
                    if (success) {
                        MessageBox.showInfo("Success", "Thank you! Your review has been submitted.");
                        // Refresh table to reflect 'rated' status and hide button
                        loadData(SessionManager.getUserID());
                    } else {
                        MessageBox.showError("Error", "Failed to submit review. Please try again.");
                    }
                } catch (Exception ex) {
                    MessageBox.showError("Error", "Unexpected error: " + ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void loadData(String householdId) {
        try {
            CustomList<FutureWork> list = service.getFutureWorksForHousehold(householdId);
            ObservableList<FutureWork> obsList = FXUtil.toObservableList(list);
            futureWorkTable.getItems().setAll(obsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
