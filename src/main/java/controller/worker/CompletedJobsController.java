package controller.worker;

import app.PastWorkService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.PastTask;

import java.util.List;

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
            colWorkId.setCellValueFactory(new PropertyValueFactory<>("taskId"));
            colService.setCellValueFactory(new PropertyValueFactory<>("title"));
            colCustomer.setCellValueFactory(new PropertyValueFactory<>("householdId"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
            colReview.setCellValueFactory(new PropertyValueFactory<>("review"));


            loadData(1); // replace 1 with logged-in workerId
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(int workerId) {
        try {
            List<PastTask> list = service.getPastWorksForWorker(workerId);
            completedTable.getItems().setAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
