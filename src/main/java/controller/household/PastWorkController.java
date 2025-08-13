package controller.household;

import app.household.FutureWorkService;
import app.household.OngoingWorkService;
import app.household.PastWorkService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.FutureWork;
import model.OngoingWork;
import model.PastWork;
import model.SessionManager;
import util.MessageBox;

import java.sql.SQLException;
import java.util.List;

public class PastWorkController {
    @FXML
    private TableView<PastWork> completedTable;

    @FXML
    private TableColumn<PastWork, Integer> colWorkId;

    @FXML
    private TableColumn<PastWork, String> colService;

    @FXML
    private TableColumn<PastWork, String> colWorker;

    @FXML
    private TableColumn<PastWork, String> colDate;

    @FXML
    private TableColumn<PastWork, Integer> colRating;

    @FXML
    private TableColumn<PastWork, String> colReview;

    @FXML
    private TableColumn<PastWork, Double> colAmount;

    private ObservableList<PastWork> pastWorkData = FXCollections.observableArrayList();
    public void initialize() {
        try {
///  remain to learn this
            colWorkId.setCellValueFactory(new PropertyValueFactory<>("workId"));
            colService.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
            colWorker.setCellValueFactory(new PropertyValueFactory<>("workerName"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
            colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
            colReview.setCellValueFactory(new PropertyValueFactory<>("review"));
            colAmount.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));



            // Set ObservableList once to TableView
            completedTable.setItems(pastWorkData);

            // Load initial data
            loadPastWorks();

        } catch (Exception e) {
            e.printStackTrace();
            MessageBox.showError("Loading Error", "Could not load future work data.");
        }
    }

    private void loadPastWorks() {
        try {
            String username = SessionManager.username;
            PastWorkService service = new PastWorkService();
            List<PastWork> list = service.getPastWorkForUser(username);

            // Update existing ObservableList (clears and adds new items)
            pastWorkData.setAll(list);

        } catch (SQLException e) {
            e.printStackTrace();
            MessageBox.showError("Loading Error", "Failed to load past(completed) work data.");
        }
    }
}
