package controller.worker;

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

public class CompletedJobsController {
    @FXML private TableView<PastTask> completedTable;
    @FXML private TableColumn<PastTask, Integer> colWorkId;
    @FXML private TableColumn<PastTask, String> colService;
    @FXML private TableColumn<PastTask, Integer> colCustomer;
    @FXML private TableColumn<PastTask, java.time.LocalDate> colDate;
    @FXML private TableColumn<PastTask, Integer> colRating;
    @FXML private TableColumn<PastTask, String> colReview;
    @FXML private TableColumn<PastTask, java.math.BigDecimal> colAmount;

    private PastWorkService service;

    @FXML
    public void initialize() {
        try {
            /// checking
//            System.out.println("Inside controller");
            service = new PastWorkService();
            colWorkId.setCellValueFactory(new PropertyValueFactory<>("taskId"));
            colService.setCellValueFactory(new PropertyValueFactory<>("title"));
            colCustomer.setCellValueFactory(new PropertyValueFactory<>("householdId"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
            colReview.setCellValueFactory(new PropertyValueFactory<>("review"));


            loadData(SessionManager.getUserID()); // replace 1 with logged-in workerId
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(String workerId) {
        try {
//            System.out.println("Going to service");
            CustomList<PastTask> list = service.getPastWorksForWorker(workerId);
            ObservableList<PastTask> obsList = FXUtil.toObservableList(list);
            completedTable.getItems().setAll(obsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
