package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import app.UserServices;
import model.SessionManager;
import util.MessageBox;
import util.OTP;

public class RegisterController {

    /// Basic fields
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
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
    @FXML private TextField pinCode;

    /// worker specific fields
    @FXML private ComboBox<String> category;
    @FXML private TextField workArea;
    @FXML private TextField experience;

    @FXML private VBox dynamicFields;

    @FXML private Hyperlink goToLoginLink;

    private boolean otpSent = false;
    private String otp = "";
    private ToggleGroup accountTypeGroup;

    UserServices userServices = new UserServices();

    @FXML
    public void initialize() {
        accountTypeGroup = new ToggleGroup();
        householdRadio.setToggleGroup(accountTypeGroup);
        workerRadio.setToggleGroup(accountTypeGroup);

        goToLoginLink.setOnAction(event -> {
            try {
                FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
                Parent loginRoot = loader.load();
                Stage stage = (javafx.stage.Stage) goToLoginLink.getScene().getWindow();
                stage.setScene(new javafx.scene.Scene(loginRoot, 1400, 800));
            } catch (Exception e) {
                System.out.println("Error loading Login Page: " + e.getMessage());
                MessageBox.showError("Error", "Failed to load login page.");
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
        address.setMaxWidth(400);
        address.setStyle("-fx-background-radius: 10; -fx-padding: 10; -fx-pref-height: 40;");

        pinCode = new TextField();
        pinCode.setPromptText("PinCode");
        pinCode.setMaxWidth(400);
        pinCode.setStyle("-fx-background-radius: 10; -fx-padding: 10; -fx-pref-height: 40;");

        city = new TextField();
        city.setPromptText("City");
        city.setMaxWidth(400);
        city.setStyle("-fx-background-radius: 10; -fx-padding: 10; -fx-pref-height: 40;");

        dynamicFields.getChildren().addAll(address, pinCode, city);
    }

    /// loading worker specific fields when worker radio button is selected
    private void loadWorkerFields() {
        dynamicFields.getChildren().clear();

        category = new ComboBox<>();
        category.getItems().addAll("Plumber", "Electrician", "Cleaner", "Painter", "Cook", "Gardener");
        category.setPromptText("Select Category");
        category.setMaxWidth(400);
        category.setStyle("-fx-background-color: #F5F5F5; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: #1B4242; " +
                "-fx-border-width: 1; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 5 10;");

        workArea = new TextField();
        workArea.setPromptText("Preferred Location to work");
        workArea.setMaxWidth(400);
        workArea.setStyle("-fx-background-radius: 10; -fx-padding: 10; -fx-pref-height: 40;");

        experience = new TextField();
        experience.setPromptText("Years of Experience");
        experience.setMaxWidth(400);
        experience.setStyle("-fx-background-radius: 10; -fx-padding: 10; -fx-pref-height: 40;");

        dynamicFields.getChildren().addAll(category, workArea, experience);
    }

    public boolean handleRegister(){
        String userID = ""; // call userID generator method here
        SessionManager.setUserID(userID);
        if (householdRadio.isSelected()) {
            return userServices.registerHousehold(userID, firstNameField.getText(), lastNameField.getText(), emailField.getText(),passwordField.getText(), ageField.getText(), genderCombo.getValue(), address.getText(), city.getText(), pinCode.getText());
        } else {
            return userServices.registerWorker(userID, firstNameField.getText(), lastNameField.getText(), emailField.getText(), passwordField.getText(), ageField.getText(), genderCombo.getValue(), category.getValue(), experience.getText(), workArea.getText());
        }
    }

    /// Handle OTP button click
    public void handleOtp() {
        if (!validateEmptyFields()) return;
        if (!otpSent) {
            // Logic to send OTP
            System.out.println("Sending OTP to: " + emailField.getText());

            otp = OTP.sendOtp(emailField.getText());
            System.out.println("OTP sent: " + otp);

            otpField.setVisible(true);
            otpField.setManaged(true);
            otpButton.setText("Register");
            otpSent = true;
        } else {
            // Logic to verify OTP and register user
            if (!otpField.getText().trim().isEmpty()) {
                System.out.println("Verifying OTP: " + otpField.getText());

                if (otpField.getText().equals(otp)) {
                    System.out.println("OTP verified successfully.");
                    if (handleRegister()) {
                        System.out.println("Registration successful!");
                        MessageBox.showInfo("Registration Successful", "You have been registered successfully.");

                        // Load the dashboard or next page
                    } else {
                        System.out.println("something unexpected happened, please try again.");
                    }
                } else {
                    System.out.println("Invalid OTP. Please try again.");
                    MessageBox.showAlert("Invalid OTP", "The OTP you entered is incorrect. Please try again.");
                }
            } else {
                System.out.println("Registration failed. Please check your details.");
                MessageBox.showAlert("Missing OTP", "Please enter the OTP.");
            }
        }
    }

    /// Validate all fields before sending OTP or registering (except OTP)
    private boolean validateEmptyFields() {
        if (firstNameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
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
            if (address.getText().trim().isEmpty() || city.getText().trim().isEmpty() || pinCode.getText().trim().isEmpty()) {
                MessageBox.showAlert("Household Registration", "Please fill in household details.");
                return false;
            }
        } else if (workerRadio.isSelected()) {
            if (category.getValue() == null || experience.getText().trim().isEmpty() || workArea.getText().trim().isEmpty()) {
                MessageBox.showAlert("Worker Registration", "Please fill in worker details.");
                return false;
            }
        }
        return true;
    }
}