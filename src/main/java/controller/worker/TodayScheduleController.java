package controller.worker;

import app.MessageService;
import app.worker.TodayService;
import datastructures.CustomList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.FutureWork;
import model.SessionManager;
import util.FXUtil;

public class TodayScheduleController {

    @FXML private TableView<FutureWork> todayTable;
    @FXML private TableColumn<FutureWork, Integer> colTaskId;
    @FXML private TableColumn<FutureWork, String> colCustomerId;
    @FXML private TableColumn<FutureWork, String> colTitle;
    @FXML private TableColumn<FutureWork, String> colAddress;
    @FXML private TableColumn<FutureWork, String> colStartTime;
    @FXML private TableColumn<FutureWork, String> colEndTime;
    @FXML private TableColumn<FutureWork, Double> colCost;
    @FXML private TableColumn<FutureWork, Void> colAction;

    private TodayService jobService = new TodayService();

    @FXML
    public void initialize() {
        setupTable();
        loadJobsFromDB();
    }

    private void setupTable() {
        colTaskId.setCellValueFactory(new PropertyValueFactory<>("taskId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("householdId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        // Action button for marking complete
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button completeBtn = new Button("Complete");

            {
                completeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
                completeBtn.setOnAction(event -> {
                    FutureWork job = getTableView().getItems().get(getIndex());
                    markJobAsComplete(job);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : completeBtn);
            }
        });
    }

    private void loadJobsFromDB() {
        CustomList<FutureWork> list =  jobService.getOngoingJobsForToday();
        ObservableList<FutureWork> jobs = FXUtil.toObservableList(list);
        todayTable.setItems(jobs);
    }

    private void markJobAsComplete(FutureWork job) {
        boolean success = jobService.markJobAsComplete(job.getTaskId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Job Status");
        alert.setHeaderText(null);
        alert.setContentText(success
                ? "Work ID " + job.getTaskId() + " marked as completed!"
                : "Failed to update job status. Try again.");
        alert.showAndWait();

        if (success) {
            MessageService ms = new MessageService();
            ms.sendMessage(job.getHouseholdId(), SessionManager.getUserID(),"Work is completed please give review");
            todayTable.getItems().remove(job);
        }
    }
}
