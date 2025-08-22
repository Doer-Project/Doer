package controller.household;

import app.FutureWorkService;
import datastructures.CustomList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.FutureWork;
import model.SessionManager;
import util.FXUtil;
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

            loadData(SessionManager.getUserID()); // replace 3 with logged-in householdId
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(String householdId) {
        try {
            CustomList<FutureWork> list = service.getFutureWorksForHousehold(householdId);
            // Convert CustomList -> ObservableList
            ObservableList<FutureWork> obsList = FXUtil.toObservableList(list);
            futureWorkTable.getItems().setAll(obsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
