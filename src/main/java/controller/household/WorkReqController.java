package controller.household;

import app.household.WorkReqService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.WorkRequest;
import util.MessageBox;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkReqController {

    @FXML private TextField titleField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker requestDatePicker;
    @FXML private TextField cityTextField;
    @FXML private TextField areaTextField;
    @FXML private TextField budgetField;


    // Field-level initialization of service using a factory method
    private WorkReqService service = createService();

    private static WorkReqService createService() {
        try {
            return new WorkReqService();
        } catch (Exception e) {
            MessageBox.showAlert("DB Error", "Failed to connect to database.");
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void initialize() {
        // Populate category options
        categoryComboBox.getItems().addAll("Plumbing", "Electrical", "Cleaning", "Painting", "Other");

        // Disable save button if service failed to initialize
        if (service == null) {
            MessageBox.showAlert("DB Error", "Service not available. Cannot save requests.");

        }
    }

    @FXML
    private void saveRequest() {
        if (service == null) {
            MessageBox.showAlert("DB Error", "Service not available. Cannot save requests.");
            return;
        }

        String title = titleField.getText();
        String description = descriptionTextArea.getText();
        int categoryId = categoryComboBox.getSelectionModel().getSelectedIndex() + 1; // IDs start at 1
        String city = cityTextField.getText();
        String area = areaTextField.getText();
        double budget = parseDouble(budgetField.getText());
        LocalDate date = requestDatePicker.getValue();
        String householdId = SessionManager.username;

        WorkRequest request = WorkReqService.createRequest(
                title, description, categoryId, city, area, budget, date, householdId
        );

        if (request != null) {
            boolean success = service.saveWorkRequest(request);
            if (success) {
                MessageBox.showInfo("Success", "Work request saved successfully!");
                resetForm();
            } else {
                MessageBox.showAlert("DB Error", "Failed to save work request.");
            }
        } else {
            MessageBox.showAlert("Validation Error", "Please fill all fields correctly.");
        }
    }

    @FXML
    private void resetForm() {
        titleField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        descriptionTextArea.clear();
        requestDatePicker.setValue(null);
        cityTextField.clear();
        areaTextField.clear();
        budgetField.clear();
    }

    private double parseDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return -1; // invalid number will fail factory validation
        }
    }

    private LocalTime parseTime(String text) {
        try {
            return LocalTime.parse(text);
        } catch (Exception e) {
            return null; // invalid time will fail factory validation
        }
    }
}
