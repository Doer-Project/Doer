package controller.household;

import app.FutureWorkService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.FutureWork;
import model.SessionManager;

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
            System.out.println("controller");
            service = new FutureWorkService();

            colTaskId.setCellValueFactory(new PropertyValueFactory<>("taskId"));
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colWorkerId.setCellValueFactory(new PropertyValueFactory<>("workerId"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            loadData(SessionManager.getUserID()); // replace 3 with logged-in householdId
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(String householdId) {
        try {
            System.out.println("going to service");
            List<FutureWork> list = service.getFutureWorksForHousehold(householdId);
            futureWorkTable.getItems().setAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
