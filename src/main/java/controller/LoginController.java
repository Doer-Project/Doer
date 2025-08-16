package controller;

import app.UserServices;
import database.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import model.SessionManager;
import util.MessageBox;
import util.Validations;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField otpField;
    @FXML private Button sendOtpBtn;
    @FXML private Hyperlink goToRegisterLink;

    private boolean otpSent = false;

    @FXML
    public void initialize() {
        goToRegisterLink.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegisterPage.fxml"));
                Parent registerRoot = loader.load();
                Stage stage = (Stage) goToRegisterLink.getScene().getWindow();
                stage.setScene(new Scene(registerRoot, 1400, 900));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public boolean handleLogin() {
        if (Validations.isValidEmail(emailField.getText())) {
            emailField.setText(UserServices.getUserEmail(emailField.getText()));
            return handleLogin();
        }
        else {
            // Validate username and password fields
            return verifyCredentials(emailField.getText(), passwordField.getText());
        }
    }

    private boolean verifyCredentials(String username, String password) {
        if (UserServices.verifyUser(username, password)) {
            System.out.println("Login successful for user: " + username);
            ///  Store the logged-in username globally for session management
            SessionManager.username = username;
            return true;
        } else {
            System.out.println("Login failed for user: " + username);
            return false;
        }
    }


    public void handleOtpOrLogin(ActionEvent actionEvent) {
        if (!validateLoginFields() || !handleLogin()) return;
        String username = "", password = "";
        if (!otpSent) {
            username = emailField.getText();
            password = passwordField.getText();

            // Send OTP logic

            System.out.println("Sending OTP to: " + emailField.getText());
            otpField.setVisible(true);
            otpField.setManaged(true);
            sendOtpBtn.setText("Login");
            otpSent = true;
        } else {
            if (!username.equals(emailField.getText()) || !password.equals(passwordField.getText())) {
                System.out.println("Username or Password has changed since OTP was sent.");
                MessageBox.showAlert("Error", "Username or Password has changed since OTP was sent.");
                return;
            }

            // Verify OTP logic

            System.out.println("Verifying OTP: " + otpField.getText());
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
