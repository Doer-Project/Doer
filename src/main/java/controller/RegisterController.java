package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import util.MessageBox;

public class RegisterController {

    /// Basic fields
    @FXML private TextField nameField;
    @FXML private TextField userNameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private TextField emailField;
    @FXML private TextField otpField;
    @FXML private Button otpButton;

    @FXML private RadioButton householdRadio;
    @FXML private RadioButton workerRadio;

    /// household specific fields
    @FXML private TextField address;
    @FXML private TextField city;

    /// worker specific fields
    @FXML private ComboBox<String> category;
    @FXML private TextField experience;

    @FXML private VBox dynamicFields;

    @FXML private Hyperlink goToLoginLink;

    private boolean otpSent = false;
    private ToggleGroup accountTypeGroup;

    @FXML
    public void initialize() {
        // Make radio buttons mutually exclusive
        accountTypeGroup = new ToggleGroup();
        householdRadio.setToggleGroup(accountTypeGroup);
        workerRadio.setToggleGroup(accountTypeGroup);

        goToLoginLink.setOnAction(event -> {
            try {
                FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
                Parent loginRoot = loader.load();
                Stage stage = (javafx.stage.Stage) goToLoginLink.getScene().getWindow();
                stage.setScene(new javafx.scene.Scene(loginRoot, 600, 500));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        householdRadio.setOnAction(e -> loadHouseholdFields());
        workerRadio.setOnAction(e -> loadWorkerFields());
    }

    /// loading household specific fields when household radio button is selected
    private void loadHouseholdFields() {
        dynamicFields.getChildren().clear();

        address = new TextField();
        address.setPromptText("Address");
        address.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10; -fx-padding: 10;");

        city = new TextField();
        city.setPromptText("City");
        city.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10; -fx-padding: 10;");

        dynamicFields.getChildren().addAll(address, city);
    }

    /// loading worker specific fields when worker radio button is selected
    private void loadWorkerFields() {
        dynamicFields.getChildren().clear();

        category = new ComboBox<>();
        category.getItems().addAll("Plumber", "Electrician", "Cleaner", "Cook", "Gardener");
        category.setPromptText("Select Category");
        category.setMaxWidth(300);
        category.setStyle("-fx-background-color: #F5F5F5; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: #1B4242; " +
                "-fx-border-width: 1; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 5 10;");

        experience = new TextField();
        experience.setPromptText("Years of Experience");
        experience.setMaxWidth(300);
        experience.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 10; -fx-padding: 10;");

        dynamicFields.getChildren().addAll(category, experience);
    }

    /// Handle OTP button click
    public void handleOtp(ActionEvent actionEvent) {
        if (!validateFields()) return;
        if (!otpSent) {
            // Logic to send OTP
            System.out.println("Sending OTP to: " + emailField.getText());
            otpField.setVisible(true);
            otpField.setManaged(true);
            otpButton.setText("Register");
            otpSent = true;
        } else {
            // Logic to verify OTP and register user
            if (!otpField.getText().trim().isEmpty()) {
                System.out.println("Verifying OTP: " + otpField.getText());
                System.out.println("Registration successful!");
                // Redirect to login page or main application page
                // You can add your logic here
            } else {
                System.out.println("Registration failed. Please check your details.");
                MessageBox.showAlert("Missing OTP", "Please enter the OTP.");
            }
        }
    }

    /// Validate all fields before sending OTP or registering (except OTP)
    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty() ||
            userNameField.getText().trim().isEmpty() ||
            ageField.getText().trim().isEmpty() ||
            passwordField.getText().trim().isEmpty() ||
            confirmPasswordField.getText().trim().isEmpty() ||
            genderCombo.getValue() == null ||
            emailField.getText().trim().isEmpty()) {
            MessageBox.showAlert("Missing Information", "Please fill in all fields before requesting OTP.");
            return false;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            MessageBox.showAlert("Password Mismatch", "Passwords do not match. Please try again.");
            return false;
        }

        if (accountTypeGroup.getSelectedToggle() == null) {
            MessageBox.showAlert("Missing Selection", "Please select account type.");
            return false;
        }

        if (householdRadio.isSelected()) {
            MessageBox.showAlert("Household Registration", "Please fill in household details.");
            return !(address.getText().trim().isEmpty() && city.getText().trim().isEmpty());
        } else if (workerRadio.isSelected()) {
            MessageBox.showAlert("Worker Registration", "Please fill in worker details.");
            return !(category.getValue() == null && experience.getText().trim().isEmpty());

        }
        return false;
    }
}