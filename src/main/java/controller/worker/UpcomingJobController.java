package controller.worker;

import app.worker.UpcomingJobService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.worker.UpcomingJob;
import model.SessionManager;
import util.MessageBox;

import java.sql.SQLException;
import java.util.List;

public class UpcomingJobController {

    @FXML private TableView<UpcomingJob> upcomingTable;
    @FXML private TableColumn<UpcomingJob, String> colWorkId;
    @FXML private TableColumn<UpcomingJob, String> colService;
    @FXML private TableColumn<UpcomingJob, String> colCustomer;
    @FXML private TableColumn<UpcomingJob, String> colScheduledDate;
    @FXML private TableColumn<UpcomingJob, String> colAddress;

    private ObservableList<UpcomingJob> upcomingJobData = FXCollections.observableArrayList();

    public void initialize() {
        try {
            // Mapping table columns with model properties
            colWorkId.setCellValueFactory(new PropertyValueFactory<>("workId"));
            colService.setCellValueFactory(new PropertyValueFactory<>("service"));
            colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
            colScheduledDate.setCellValueFactory(new PropertyValueFactory<>("scheduledDate"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            // Attach ObservableList to TableView
            upcomingTable.setItems(upcomingJobData);

            // Load data initially
            loadUpcomingJobs();

        } catch (Exception e) {
            e.printStackTrace();
            MessageBox.showError("Loading Error", "Could not load upcoming jobs.");
        }
    }

    private void loadUpcomingJobs() {
        try {
            String workerId = SessionManager.getUserID(); // assuming worker is logged in
            UpcomingJobService service = new UpcomingJobService();
            List<UpcomingJob> list = service.getUpcomingJobsForWorker(workerId);

            upcomingJobData.setAll(list);

        } catch (SQLException e) {
            e.printStackTrace();
            MessageBox.showError("Loading Error", "Failed to load upcoming jobs.");
        }
    }
}
