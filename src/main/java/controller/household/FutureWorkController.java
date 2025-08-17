package controller.household;

import app.FutureWorkService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.FutureWork;

import java.util.List;

public class FutureWorkController {

    @FXML private TableView<FutureWork> futureWorkTable;
    @FXML private TableColumn<FutureWork, Integer> colTaskId;
    @FXML private TableColumn<FutureWork, String> colTitle;
    @FXML private TableColumn<FutureWork, Integer> colWorkerId;
    @FXML private TableColumn<FutureWork, java.time.LocalDate> colDate;
    @FXML private TableColumn<FutureWork, String> colStatus;
    @FXML private TableColumn<FutureWork, Integer> colRating;

    private FutureWorkService service;

    @FXML
    public void initialize() {
        try {
            service = new FutureWorkService();

            colTaskId.setCellValueFactory(new PropertyValueFactory<>("task_Id"));
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colWorkerId.setCellValueFactory(new PropertyValueFactory<>("worker_Id"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("preferred_work_date"));
            colRating.setCellValueFactory(new PropertyValueFactory<>("household_rating"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            loadData(2); // replace 3 with logged-in householdId
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(int householdId) {
        try {
            List<FutureWork> list = service.getFutureWorksForHousehold(householdId);
            futureWorkTable.getItems().setAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
