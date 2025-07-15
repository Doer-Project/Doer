package controller;

import app.userName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
                Parent registerRoot = FXMLLoader.load(getClass().getResource("/fxml/RegisterPage.fxml"));
                Stage stage = (Stage) goToRegisterLink.getScene().getWindow();
                stage.setScene(new Scene(registerRoot, 700, 650));
            } catch (Exception e) {
                e.printStackTrace();
            }

//            try {
//                Parent registerRoot = FXMLLoader.load(getClass().getResource("/fxml/RegisterPage.fxml"));
//                Stage stage = (Stage) goToRegisterLink.getScene().getWindow();
//                stage.setScene(new Scene(registerRoot, 700, 650));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        });
    }


    userName name = new userName();
    @FXML
    public void handleOtpOrLogin() {
        if (!otpSent) {
            // Send OTP logic
        String userName = emailField.getText();
        name.printName(userName);
//            System.out.println("Sending OTP to: " + emailField.getText());

            otpField.setVisible(true);
            otpField.setManaged(true);
            sendOtpBtn.setText("Login");
            otpSent = true;
        } else {
            // Login logic
            System.out.println("Verifying OTP: " + otpField.getText());
            // TODO: Actual login logic here (check OTP, email, password, etc.)
        }
    }

    public void passwordField(ActionEvent event) {
    }
}
