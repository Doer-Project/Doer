package controller;

import app.UserProfileService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import util.MessageBox;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EditProfileController {

    @FXML private TextField txtFullName, txtAddress;
    @FXML private Label txtEmail, lblUserType, lblGender;
    @FXML private ImageView profileImage;
    @FXML private StackPane rootPane;

    private static final String DEFAULT_IMAGE = "/images/default.jpeg";

    private String userId;
    private String userType;

    @FXML
    public void initialize() {
        try {
            userId = model.SessionManager.getUserID();
//            if (userId == null) {
//                MessageBox.showError("Load Error", "No user ID found.");
//                return;
//            }

            // Fetch user data: [firstName, lastName, email, address/work_area, userType, gender]
            List<String> userData = UserProfileService.getUserDataList(userId);
            if (userData != null && userData.size() >= 6) {
                txtFullName.setText(userData.get(0) + " " + userData.get(1));
                txtAddress.setText(userData.get(3));
                userType = userData.get(4);
                try {
                    if (getClass().getResource(DEFAULT_IMAGE) == null) {
                        System.out.println("Default image not found at " + DEFAULT_IMAGE);
                    }

                    // Load default image
                    profileImage.setImage(new Image(getClass().getResourceAsStream(DEFAULT_IMAGE)));
                } catch (Exception e) {
                    System.out.println("Failed to load default image: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            MessageBox.showError("Load Error", "Error loading profile:\n" + e.getMessage());
        }
    }

    @FXML
    public void cancleBtn(ActionEvent event) {
        try {
            Parent profileView = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
            rootPane.getChildren().setAll(profileView);
        } catch (IOException e) {
            MessageBox.showError("Navigation Error", e.getMessage());
        }
    }

    @FXML
    public void saveBtn(ActionEvent event) {
        try {
            String[] parts = txtFullName.getText().trim().split(" ", 2);
            String firstName = parts[0];
            String lastName = (parts.length > 1) ? parts[1] : "";
            String addressOrWorkArea = txtAddress.getText().trim();
//            String userType = lblUserType.getText();
//            System.out.println(userType);

            // Confirm save
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Do you want to save changes?",
                    ButtonType.YES, ButtonType.NO);
            confirmAlert.setTitle("Confirm Changes");
            confirmAlert.showAndWait();
            if (confirmAlert.getResult() != ButtonType.YES) return;

            // Update DB
            boolean success = UserProfileService.updateUserProfile(userId, firstName, lastName, addressOrWorkArea, userType);

            if (success) {
                MessageBox.showInfo("Success", "Profile updated successfully!");
                Parent profileView = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
                rootPane.getChildren().setAll(profileView);
            } else {
                MessageBox.showAlert("Update Failed", "Could not update profile.");
            }

        } catch (Exception e) {
            MessageBox.showError("Save Error", e.getMessage());
        }
    }

    @FXML
    private void changePhotoBtn(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(profileImage.getScene().getWindow());
        if (selectedFile != null) {
            profileImage.setImage(new Image(selectedFile.toURI().toString()));
            // Store path or copy file to your app folder for saving in DB
        }
    }

}
