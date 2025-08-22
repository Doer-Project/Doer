package controller.household;

import app.household.OngoingWorkService;
import datastructures.CustomList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.household.OngoingWork;
import util.FXUtil;

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
    }

    private void loadTableData(String requestId) {
        try {
            OngoingWorkService service = new OngoingWorkService();
            CustomList<OngoingWork> workerList = service.getAllOngoingWorkers(requestId); // fetch all workers

            if (workerList.size() > 10) {
                workerList = workerList.subList(0, 10);
            }
            System.out.println(workerList.size());
            // Convert CustomList -> ObservableList
            ObservableList<OngoingWork> obsList = FXUtil.toObservableList(workerList);
            detailsTable.getItems().clear();
            detailsTable.getItems().addAll(obsList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOngoingWork(OngoingWork work) {
        this.ongoingWork = work;
        if (ongoingWork != null) {
            loadTableData(ongoingWork.getRequest_id()+"");
        }
    }
}
