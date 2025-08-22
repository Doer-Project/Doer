package controller.household;

import app.household.OngoingWorkService;
import util.MessageBox;
import datastructures.CustomList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.household.OngoingWork;
import util.FXUtil;

import java.util.ArrayList;
import java.util.List;

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

    private List<Button> hireButtons = new ArrayList<>();

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

            // Track all Hire buttons and set their actions
            hireButtons.clear();
            for (OngoingWork work : obsList) {
                Button btn = work.getSelectButton().get();
                if (btn != null) {
                    hireButtons.add(btn);
                }
            }
            for (int i = 0; i < obsList.size(); i++) {
                OngoingWork work = obsList.get(i);
                Button btn = work.getSelectButton().get();
                if (btn == null || btn.isDisabled()) continue;
                btn.setOnAction(e -> {
                    boolean hired = service.hireWorker(
                        work.getRequest_id(),
                        work.getWorkerId(),
                        work.getStartTime(),
                        work.getEndTime(),
                        Double.parseDouble(work.getExpectedCost())
                    );
                    if (hired) {
                        btn.setText("Hired");
                        for (Button otherBtn : hireButtons) {
                            if (otherBtn != btn) {
                                otherBtn.setDisable(true);
                            }
                        }
                    } else {
                        MessageBox.showError("Error", "Failed to hire worker. Please try again.");
                    }
                });
            }
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
