package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

import util.MessageBox;

public class RegisterController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private ComboBox<String> genderCombo;
    @FXML
    private TextField emailField;
    @FXML
    private TextField otpField;
    @FXML
    private Button otpButton;

    @FXML
    private RadioButton householdRadio;
    @FXML
    private RadioButton workerRadio;
    @FXML
    private VBox dynamicFields;

    @FXML
    private VBox registerForm;

    private boolean otpSent = false;

    @FXML
    private Hyperlink goToLoginLink;

    @FXML
    public void initialize() {
        householdRadio.setOnAction(e -> loadHouseholdFields());
        workerRadio.setOnAction(e -> loadWorkerFields());

        goToLoginLink.setOnAction(event -> {
            try {
                Parent loginRoot = FXMLLoader.load(getClass().getResource("/fxml/LoginPage.fxml"));
                Stage stage = (Stage) goToLoginLink.getScene().getWindow();
                stage.setScene(new Scene(loginRoot, 600, 500));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void handleRegister() {


        String name = nameField.getText();
        String userName = emailField.getText();
        String ageText = ageField.getText();
        String otp = otpField.getText();
        String gender = genderCombo.getValue();
        String role = householdRadio.isSelected() ? "Household" : workerRadio.isSelected() ? "Worker" : null;
//        String address = String.valueOf()

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            MessageBox.showError("Please enter a valid number for age.");
            return;

        }
//        User user = new User(name, userName, age, gender);

        ///  for 2 section of the dahsbord
        if (role.equals("Household")) {

        } else if (role.equals("Worker")) {

        }

        System.out.println("Register ✅");
    }

    @FXML
    public void handleOtp() {
        if (!otpSent) {
            System.out.println("Sending OTP to: " + emailField.getText());

            otpField.setVisible(true);
            otpField.setManaged(true);
            otpButton.setText("Register");
            otpSent = true;
        } else {
            System.out.println("Verifying OTP: " + otpField.getText());
            handleRegister();
        }
    }

    private void loadHouseholdFields() {
        dynamicFields.getChildren().clear();

        TextField address = new TextField();
        address.setPromptText("Full Address");
        address.setMaxWidth(300);
        address.setStyle("-fx-background-radius: 10; -fx-padding: 8;");

        TextField city = new TextField();
        city.setPromptText("City");
        city.setMaxWidth(300);
        city.setStyle("-fx-background-radius: 10; -fx-padding: 8;");

        dynamicFields.getChildren().addAll(address, city);
    }

    private void loadWorkerFields() {
        dynamicFields.getChildren().clear();

        ComboBox<String> category = new ComboBox<>();
        category.getItems().addAll("Electrician", "Plumber", "Cleaner", "Cook", "Painter");
        category.setPromptText("Select Work Category");
        category.setMaxWidth(300);
        category.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-padding: 10; -fx-border-color: #1B4242; -fx-border-radius: 10; -fx-pref-height: 40;");

        TextField experience = new TextField();
        experience.setPromptText("Years of Experience");
        experience.setMaxWidth(300);
        experience.setStyle("-fx-background-radius: 10; -fx-padding: 8;");

        dynamicFields.getChildren().addAll(category, experience);
    }
}
