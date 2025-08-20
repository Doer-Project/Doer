package controller;

import app.UserServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.SessionManager;
import util.MessageBox;

public class ChangePasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private VBox otpContainer;

    private TextField otpField;

    UserServices userServices = new UserServices();

    @FXML
    private void handleSendOtp() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (!isEmailRegistered(email)) {
            MessageBox.showError("Invalid Email", "This email is not registered!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            MessageBox.showError("Password Mismatch", "Passwords do not match!");
            return;
        }

        if (otpField == null) {
            otpField = new TextField();
            otpField.setPromptText("Enter OTP here");
            otpContainer.getChildren().add(otpField);
        } else {
            MessageBox.showAlert("Already Sent", "OTP field already added!");
        }
    }

    private boolean isEmailRegistered(String email) {
        String userId = SessionManager.getUserID();
        return userServices.getEmailByUserId(userId).equals(email);
    }
}
