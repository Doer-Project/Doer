package controller.worker;

import app.FutureWorkService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.FutureWork;

import java.util.List;

public class UpcomingJobController {

    @FXML private TableView<FutureWork> upcomingTable;
    @FXML private TableColumn<FutureWork, Integer> colTaskId;
    @FXML private TableColumn<FutureWork, String> colTitle;
    @FXML private TableColumn<FutureWork, Integer> colHouseholdId;
    @FXML private TableColumn<FutureWork, java.time.LocalDate> colDate;
    @FXML private TableColumn<FutureWork, String> colAddress;
    @FXML private TableColumn<FutureWork, String> colStatus;
    @FXML private TableColumn<FutureWork, Integer> colRating;

    private FutureWorkService service;

    @FXML
    public void initialize() {
        try {
            System.out.println("In controller");
            service = new FutureWorkService();

            colTaskId.setCellValueFactory(new PropertyValueFactory<>("taskId"));
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colHouseholdId.setCellValueFactory(new PropertyValueFactory<>("householdId"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

            loadData(4); // replace 2 with logged-in workerId
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(int workerId) {
        try {
            List<FutureWork> list = service.getFutureWorksForWorker(workerId);
            upcomingTable.getItems().setAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
