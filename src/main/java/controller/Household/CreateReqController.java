package controller.household;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CreateReqController {

    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DatePicker preferredDatePicker;
    @FXML
    private TextField addressTextField, cityTextField, pinCodeTextField;
    @FXML
    private Button resetButton, submitButton;

    @FXML

    public void initialize() {
        categoryComboBox.getItems().addAll("Plumbing", "Electrical", "Cleaning", "Painting", "Cooking", "Gardening");

        resetButton.setOnAction(event -> resetForm());
        submitButton.setOnAction(event -> submitForm());
    }

    private void submitForm() {

    }

    private void resetForm() {
        categoryComboBox.setValue(null);
        descriptionTextArea.clear();
        preferredDatePicker.setValue(null);
        addressTextField.clear();
        cityTextField.clear();
        pinCodeTextField.clear();
    }
}
