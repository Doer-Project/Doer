package controller.household;

import app.PastWorkService;
import datastructures.CustomList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.PastTask;
import model.SessionManager;
import util.FXUtil;


public class PastWorkController {
    @FXML private TableView<PastTask> completedTable;
    @FXML private TableColumn<PastTask, Integer> colWorkId;
    @FXML private TableColumn<PastTask, String> colService;
    @FXML private TableColumn<PastTask, Integer> colWorker;
    @FXML private TableColumn<PastTask, java.time.LocalDate> colDate;
    @FXML private TableColumn<PastTask, Integer> colRating;
    @FXML private TableColumn<PastTask, String> colReview;

    private PastWorkService service;

    @FXML
    public void initialize() {
        try {
            service = new PastWorkService();
            colWorkId.setCellValueFactory(new PropertyValueFactory<>("taskId"));
            colService.setCellValueFactory(new PropertyValueFactory<>("title"));
            colWorker.setCellValueFactory(new PropertyValueFactory<>("workerId"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
            colReview.setCellValueFactory(new PropertyValueFactory<>("review"));

            ///  user_id will pass in this class and use here
            loadData(SessionManager.getUserID()); // replace 1 with logged-in householdId
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(String householdId) {
        try {
            CustomList<PastTask> list = service.getPastWorksForHousehold(householdId);
            ObservableList<PastTask> obsList = FXUtil.toObservableList(list);
            completedTable.getItems().setAll(obsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
