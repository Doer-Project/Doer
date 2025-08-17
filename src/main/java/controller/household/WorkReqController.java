package controller.household;

import app.household.WorkReqService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.SessionManager;
import util.MessageBox;

import java.time.LocalDate;

public class WorkReqController {

    @FXML private TextField titleField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker requestDatePicker;
    @FXML private ComboBox<String> cityTextField;
    @FXML private TextField areaTextField;
    @FXML private TextField pinCodeTextField;
    @FXML private Button resetButton;
    @FXML private Button submitButton;

    // Field-level initialization of service using a factory method
    private final WorkReqService service = new WorkReqService();

    @FXML
    public void initialize() {
        resetButton.setOnAction(event -> resetForm());
        submitButton.setOnAction(event -> saveRequest());

        // set default values for location fields (yet to be implemented)
    }

    @FXML
    private void saveRequest() {
        String title = titleField.getText();
        String category = categoryComboBox.getValue();
        String description = descriptionTextArea.getText();
        LocalDate date = requestDatePicker.getValue();
        String area = areaTextField.getText();
        String city = cityTextField.getValue();
        String pinCode = pinCodeTextField.getText();
        String householdId = SessionManager.getUserID();

        if (service.saveWorkRequest(title, description, category, city, area, pinCode, date, householdId)) {
            System.out.println("Work request created successfully!");
            MessageBox.showInfo("Success", "Work request created successfully!");
            resetForm();
        } else {
            System.out.println("Failed to create work request.");
        }
    }

    @FXML
    private void resetForm() {
        titleField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        descriptionTextArea.clear();
        requestDatePicker.setValue(null);
        cityTextField.getSelectionModel().clearSelection();
        areaTextField.clear();
        pinCodeTextField.clear();
    }
}
