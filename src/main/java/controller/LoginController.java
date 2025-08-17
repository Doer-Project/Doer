package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



import app.UserServices;
import model.SessionManager;
import util.MessageBox;
import util.OTP;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField otpField;
    @FXML private Button sendOtpBtn;
    @FXML private Hyperlink goToRegisterLink;

    private boolean otpSent = false;
    private String otp = "";

    UserServices userServices = new UserServices();

    @FXML
    public void initialize() {
        goToRegisterLink.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegisterPage.fxml"));
                Parent registerRoot = loader.load();
                Stage stage = (Stage) goToRegisterLink.getScene().getWindow();
                stage.setScene(new Scene(registerRoot, 1400, 800));
            } catch (Exception e) {
                System.out.println("Error loading Register Page: " + e.getMessage());
                MessageBox.showAlert("Error", "Could not load the registration page. Please try again later.");
            }
        });
    }

    public void handleOtpOrLogin() {
        if (!validateLoginFields() || !verifyCredentials(emailField.getText(), passwordField.getText())) return;

        if (!otpSent) {
            System.out.println("Sending OTP to: " + emailField.getText());

            otp = OTP.sendOtp(emailField.getText());
            System.out.println("OTP sent: " + otp);

            otpField.setVisible(true);
            otpField.setManaged(true);
            sendOtpBtn.setText("Login");
            otpSent = true;
        } else {
            System.out.println("Verifying OTP: " + otpField.getText());

            if (otpField.getText().isEmpty()) {
                System.out.println("OTP field is empty.");
                MessageBox.showAlert("Missing OTP", "Please enter the OTP sent to your email.");
            } else if (!otpField.getText().equals(otp)) {
                System.out.println("Invalid OTP entered.");
                MessageBox.showAlert("Invalid OTP", "The OTP you entered is incorrect. Please try again.");
            } else {
                SessionManager.setUserID(userServices.getUserIdByEmail(emailField.getText()));

                System.out.println("OTP verified successfully.");
                MessageBox.showInfo("Login Successful", "You have logged in successfully.");

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/household/HouseholdDB.fxml"));
                    Parent homeRoot = loader.load();
                    Stage stage = (Stage) sendOtpBtn.getScene().getWindow();
                    stage.setScene(new Scene(homeRoot, 1400, 800));
                } catch (Exception e) {
                    System.out.println("Error loading Home Page: " + e.getMessage());
                    MessageBox.showError("Error", "Failed to load home page.");
                }
            }

        }
    }

    private boolean verifyCredentials(String email, String password) {
        if (userServices.verifyUser(email, password)) {
            System.out.println("Login successful for user: " + email);
            return true;
        } else {
            System.out.println("Login failed for user: " + email);
            return false;
        }
    }

    private boolean validateLoginFields() {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            System.out.println("Email and Password cannot be empty.");
            MessageBox.showAlert("Missing Information", "Email and Password cannot be empty.");
            return false;
        }
        return true;
    }
}
