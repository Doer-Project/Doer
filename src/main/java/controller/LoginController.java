package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import main.java.util.MessageBox;

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
                Stage stage = (javafx.stage.Stage) goToRegisterLink.getScene().getWindow();
                stage.setScene(new javafx.scene.Scene(registerRoot, 700, 650));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void handleOtpOrLogin(ActionEvent actionEvent) {
        if (!validateLoginFields()) return;
        if (!otpSent) {
            // Send OTP logic
            System.out.println("Sending OTP to: " + emailField.getText());
            otpField.setVisible(true);
            otpField.setManaged(true);
            sendOtpBtn.setText("Login");
            otpSent = true;
        } else {
            // Login logic
            System.out.println("Verifying OTP: " + otpField.getText());
            // validate login credentials here
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
