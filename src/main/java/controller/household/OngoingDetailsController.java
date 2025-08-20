package controller.household;

import app.household.OngoingWorkService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.household.OngoingWork;

import java.io.IOException;

public class OngoingDetailsController {

    @FXML
    private TableView<OngoingWork> detailsTable;

    @FXML
    private TableColumn<OngoingWork, String> colWorkerId;
    @FXML
    private TableColumn<OngoingWork, String> colWorkerName;
    @FXML
    private TableColumn<OngoingWork, String> colStatus;
    @FXML
    private TableColumn<OngoingWork, String> colStartTime;
    @FXML
    private TableColumn<OngoingWork, String> colEndTime;
    @FXML
    private TableColumn<OngoingWork, String> colEstimatedCost;
    @FXML
    private TableColumn<OngoingWork, Button> colSelect;

    @FXML
    private Button backButton;

    private OngoingWork ongoingWork;

    @FXML
    private void initialize() {
        // Bind columns
        colWorkerId.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWorkerId()));
        colWorkerName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWorkerName()));
        colStatus.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStatus()));
        colStartTime.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStartTime()));
        colEndTime.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEndTime()));
        colEstimatedCost.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getExpectedCost()));
        colSelect.setCellValueFactory(data -> data.getValue().getSelectButton());

        loadTableData();

        backButton.setOnAction(e -> goBack());
    }

    private void loadTableData() {
//        try {
//            OngoingWorkService service = new OngoingWorkService();
//            List<OngoingWork> workerList = service.getAllOngoingWorkers(); // fetch all workers
//
//            // Limit to first 10 rows
//            if (workerList.size() > 10) {
//                workerList = workerList.subList(0, 10);
//            }
//
//            detailsTable.getItems().clear();
//            detailsTable.getItems().addAll(workerList);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/household/onGoing.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOngoingWork(OngoingWork work) {
        this.ongoingWork = work;
        if (ongoingWork != null) {
            detailsTable.getItems().clear();
            detailsTable.getItems().add(ongoingWork);
        }
    }
}
