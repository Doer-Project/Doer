package controller.household;

import app.household.FutureWorkService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.FutureWork;
import model.SessionManager;
import util.MessageBox;

import java.sql.SQLException;
import java.util.List;

public class FutureWorkController {
    @FXML
    private TableView<FutureWork> futureWorkTable;
    @FXML private TableColumn<FutureWork, String> colCategory;
    @FXML private TableColumn<FutureWork, String> colDate;
    @FXML private TableColumn<FutureWork, String> colTime;
    @FXML private TableColumn<FutureWork, String> colDescription;
    @FXML private TableColumn<FutureWork, String> colStatus;

    private ObservableList<FutureWork> futureWorkData = FXCollections.observableArrayList();
    public void initialize(){
        try {
///  remain to learn this
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("scheduledDate"));
            colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


            // Set ObservableList once to TableView
            futureWorkTable.setItems(futureWorkData);

            // Load initial data
            loadFutureWorks();
            
        } catch (Exception e) {
            e.printStackTrace();
            MessageBox.showError("Loading Error", "Could not load future work data.");
        }
    }

    private void loadFutureWorks() {
        try {
            String username = SessionManager.username;
            FutureWorkService service = new FutureWorkService();
            List<FutureWork> list = service.getFutureWorksForUser(username);

            // Update existing ObservableList (clears and adds new items)
            futureWorkData.setAll(list);

        } catch (SQLException e) {
            e.printStackTrace();
            MessageBox.showError("Loading Error", "Failed to load future work data.");
        }
    }
}
