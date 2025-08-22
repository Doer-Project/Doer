package controller;

import app.UserServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import model.SessionManager;
import util.MessageBox;
import util.OTP;

public class ChangePasswordController {

    @FXML
    private StackPane changeID;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField otpField;
    @FXML
    private Button otpButton;

    private String generatedOtp; // store OTP temporarily
    private boolean otpSent = false;

    UserServices userServices = new UserServices();

    @FXML
    private void handleOtpAction() {
        if (!otpSent) {
            sendOtp();
        } else {
            verifyAndChangePassword();
        }
    }

    private void sendOtp() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            MessageBox.showError("Missing Fields", "Please fill all fields before requesting OTP.");
            return;
        }

        if (!isEmailRegistered(email)) {
            MessageBox.showError("Invalid Email", "This email is not registered!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            MessageBox.showError("Password Mismatch", "Passwords do not match!");
            return;
        }

        generatedOtp = OTP.sendOtp(email);
        otpSent = true;

        otpField.setVisible(true);
        otpField.setManaged(true);
        otpButton.setText("Confirm");

        MessageBox.showInfo("OTP Sent", "An OTP has been sent to your email.");
        System.out.println("OTP sent: " + generatedOtp);
    }

    private void verifyAndChangePassword() {
        String enteredOtp = otpField.getText().trim();

        if (enteredOtp.isEmpty()) {
            MessageBox.showError("Missing OTP", "Please enter the OTP.");
            return;
        }

        if (!enteredOtp.equals(generatedOtp)) {
            MessageBox.showError("Invalid OTP", "The OTP entered is incorrect.");
            return;
        }

        // ✅ OTP matched → change password in DB
        String userId = SessionManager.getUserID();
        boolean success = userServices.updatePassword(userId, passwordField.getText().trim());

        if (success) {
            MessageBox.showInfo("Success", "Password changed successfully!");
            changeID.getScene().getWindow().hide();
        } else {
            MessageBox.showError("Error", "Something went wrong. Try again later.");
        }
    }

    private boolean isEmailRegistered(String email) {
        String userId = SessionManager.getUserID();
        return userServices.getEmailByUserId(userId).equals(email);
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        changeID.getScene().getWindow().hide();
    }
}
