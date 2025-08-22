package controller;

import app.UserProfileService;
import datastructures.CustomList;
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

public class EditProfileController {

    @FXML private TextField txtFullName, txtAddress;
    @FXML private Label txtEmail, lblUserType, lblGender;
    @FXML private ImageView profileImage;
    @FXML private StackPane rootPane;

    private static final String DEFAULT_IMAGE = "/images/default.jpeg";

    private String userId;
    private String userType;
    private File selectedImageFile = null;

    @FXML
    public void initialize() {
        try {
            userId = model.SessionManager.getUserID();
            CustomList<String> userData = app.UserProfileService.getUserDataList(userId);
            if (userData != null && userData.size() >= 6) {
                txtFullName.setText(userData.get(0) + " " + userData.get(1));
                txtAddress.setText(userData.get(3));
                userType = userData.get(4);
            }
            // Load current profile picture
            byte[] imageBytes = app.UserProfileService.getProfilePicture(userId);
            if (imageBytes != null && imageBytes.length > 0) {
                java.nio.file.Path tempPath = java.nio.file.Paths.get("src/main/resources/images/temp_profile_" + userId + ".jpg");
                java.nio.file.Files.write(tempPath, imageBytes);
                profileImage.setImage(new Image(tempPath.toUri().toString()));
            } else {
                profileImage.setImage(new Image(DEFAULT_IMAGE));
            }
        } catch (Exception e) {
            util.MessageBox.showError("Load Error", "Could not load profile: " + e.getMessage());
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

            if (selectedImageFile != null) {
                try {
                    byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedImageFile.toPath());
                    boolean updated = app.UserProfileService.updateProfilePicture(userId, imageBytes);
                    if (!updated) {
                        util.MessageBox.showError("Image Update Failed", "Could not update profile picture.");
                    }
                } catch (Exception e) {
                    util.MessageBox.showError("Image Error", "Failed to save profile picture: " + e.getMessage());
                }
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
            selectedImageFile = selectedFile;
            profileImage.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

}
